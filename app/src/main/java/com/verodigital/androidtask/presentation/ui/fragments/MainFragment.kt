package com.verodigital.androidtask.presentation.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.verodigital.androidtask.R
import com.verodigital.androidtask.data.datasource.Task
import com.verodigital.androidtask.domain.TaskListViewModel
import com.verodigital.androidtask.presentation.ui.adapters.TaskAdapter
import com.verodigital.androidtask.util.PeriodicTaskWorkerTag
import com.verodigital.androidtask.util.PermissionUtil
import com.verodigital.androidtask.util.getProgressDrawable
import com.verodigital.androidtask.util.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import okhttp3.internal.wait
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.Future

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), EasyPermissions.PermissionCallbacks {
    private val taskListViewModel: TaskListViewModel by viewModels()
    private val taskAdapter: TaskAdapter = TaskAdapter(arrayListOf())
    private var taskList: List<Task> = arrayListOf()

    private var populateTaskJob0: Job? = null
    private var populateTaskJob1: Job? = null

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var progressBar: ProgressBar? = null

    private var camPermission: Boolean = false
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imgBitmap: Bitmap? = null

    private var activityContext: Context? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progress_circular)
        setTaskAdapter()
        if (isNetworkAvailable(requireContext())) {
            var listenableFuture = WorkManager.getInstance(activityContext!!)
                .getWorkInfosByTag(PeriodicTaskWorkerTag) // ListenableFuture<List<WorkInfo>>
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Futures.addCallback(
                    listenableFuture,
                    object : FutureCallback<List<WorkInfo>> {
                        override fun onSuccess(result: List<WorkInfo>) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                populateListFromLocalDB()
                            }

                        }

                        override fun onFailure(t: Throwable) {
                            Toast.makeText(
                                activity, "PeriodicTaskWorker execution failed : ${t.message}",
                                Toast.LENGTH_LONG
                            ).show();
                        }
                    },
                    // causes the callbacks to be executed on the main (UI) thread
                    context?.mainExecutor
                )
            }


            lifecycleScope.launch {
                populateList()
            }

        } else {
            progressBar?.visibility = View.GONE
            Toast.makeText(
                activity, "Internet not available",
                Toast.LENGTH_LONG
            ).show();
        }

        taskSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTaskList(newText)
                return true
            }

        })

        qrcodeBtn.setOnClickListener(View.OnClickListener {
            if (camPermission) {
                scanImg()
            } else {
                requestPermissions()
            }

        })

        mSwipeRefreshLayout?.setOnRefreshListener {
            if(isNetworkAvailable(activityContext!!)) {
                lifecycleScope.launch {
                    populateList()
                }
            }
            else{
                progressBar?.visibility = View.GONE
                Toast.makeText(
                    activity, "Internet not available",
                    Toast.LENGTH_LONG
                ).show();
            }

            mSwipeRefreshLayout?.isRefreshing = false

        }

    }

    private fun filterTaskList(query: String?) {
        var filteredTaskList = arrayListOf<Task>()
        if (!taskList.isNullOrEmpty()) {
            lifecycleScope.launch(Dispatchers.Default) {
                filteredTaskList = taskListViewModel.filterTaskList(query, taskList)
            }

            lifecycleScope.launch(Dispatchers.Main) {
                taskAdapter.updateTasks(filteredTaskList)
            }
        }


    }


    private suspend fun populateList() {

        populateTaskJob0?.cancel()
        populateTaskJob0 = lifecycleScope.launch(Dispatchers.Main) {
            progressBar?.visibility = View.VISIBLE
            taskListViewModel.getAllLocalTasks().collectLatest {
                taskList = it
                // progressBar?.visibility = View.GONE
            }
        }

        populateTaskJob1?.cancel()
        populateTaskJob1 = lifecycleScope.launch(Dispatchers.Main) {
            progressBar?.visibility = View.VISIBLE
            if (taskList.isNotEmpty()) {
                taskAdapter.updateTasks(taskList)
                progressBar?.visibility = View.GONE
                mSwipeRefreshLayout?.isRefreshing = false
            } else {

                activityContext?.let {
                    taskListViewModel.getAllTasks(it).collectLatest {
                        taskAdapter.updateTasks(it)
                        progressBar?.visibility = View.GONE
                        taskList = it
                        mSwipeRefreshLayout?.isRefreshing = false
                        taskListViewModel.inserAllTasks(it)


                    }
                }

            }
        }

        populateTaskJob0?.join()
        populateTaskJob1?.join()

    }

    private suspend fun populateListFromLocalDB(): Boolean {

        var isListPopulated = false
        populateTaskJob0?.cancel()
        populateTaskJob0 = lifecycleScope.launch {
            taskListViewModel.getAllLocalTasks().collectLatest {
                taskList = it
                if (taskList.isNotEmpty()) {
                    taskAdapter.updateTasks(taskList)
                    isListPopulated = true
                }
            }
        }
        populateTaskJob0?.join();
        return isListPopulated
    }

    private fun setTaskAdapter() {
        recyclerViewTask.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }


    private fun scanImg() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {

            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

        } catch (e: Exception) {

            Toast.makeText(
                activity, e.message,
                Toast.LENGTH_LONG
            ).show();

        }


    }

    private fun processScan() {
        if (imgBitmap != null) {

            val mImg = InputImage.fromBitmap(imgBitmap!!, 0)
            val mScanner = BarcodeScanning.getClient(options)

            mScanner.process(mImg).addOnSuccessListener { qrcodes ->

                if (qrcodes.toString() == "[]") {
                    Toast.makeText(
                        activity, "nothing to scan",
                        Toast.LENGTH_LONG
                    ).show();

                }


                for (qrcode in qrcodes) {
                    val qrcodeType = qrcode.valueType
                    when (qrcodeType) {

                        Barcode.TYPE_TEXT -> {
                            qrcode.rawValue?.let {
                                val text = qrcode.rawValue!!.toString()
                                taskSearchView.setQuery(text, true)
                            }

                        }

                    }
                }


            }
                .addOnFailureListener {

                    Toast.makeText(
                        activity, "QRCode scanning failed",
                        Toast.LENGTH_LONG
                    ).show();

                }


        } else {


            Toast.makeText(
                activity, "No image selected to scan for QR",
                Toast.LENGTH_LONG
            ).show();


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {

            val extras: Bundle? = data?.extras
            imgBitmap = extras?.get("data") as Bitmap
            imgBitmap?.let {
                processScan()
            }

        }


    }

    private fun requestPermissions() {
        if (PermissionUtil.hasLocationPermissions(requireContext())) {
            camPermission = true
            scanImg()

        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                    this,
                    "you need to accept camera permissions to use this app for QR",
                    0,
                    Manifest.permission.CAMERA,
                )
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "you need to accept camera permissions to use this app for QR",
                    0,
                    Manifest.permission.CAMERA,

                    )
            }

        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        lifecycleScope.launch {
            camPermission = true
            scanImg()

        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


}

