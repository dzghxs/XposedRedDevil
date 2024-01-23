package com.hxs.xposedreddevil.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.hxs.xposedreddevil.adapter.FilterAdapter
import com.hxs.xposedreddevil.adapter.FilterAdapter.onItemClickListener
import com.hxs.xposedreddevil.base.BaseActivity
import com.hxs.xposedreddevil.databinding.ActivitySelectFilterBinding
import com.hxs.xposedreddevil.model.FilterBean
import com.hxs.xposedreddevil.model.FilterSaveBean
import com.hxs.xposedreddevil.service.GroupChatService
import com.hxs.xposedreddevil.utils.MessageEvent
import com.hxs.xposedreddevil.weight.LoadingDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

class SelectFilterActivity : BaseActivity(), onItemClickListener {
    private var binding: ActivitySelectFilterBinding? = null
    var loadingDialog: LoadingDialog? = null
    var filterBean: FilterBean? = null
    var bean: FilterSaveBean? = null
    var beanList: MutableList<FilterBean?> = ArrayList()
    var adapter: FilterAdapter? = null
    var gson = Gson()
    var parser = JsonParser()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectFilterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)
        DataInit()
    }

    @SuppressLint("RestrictedApi")
    private fun DataInit() {
        EventBus.getDefault().register(this)
        loadingDialog = LoadingDialog(this)
        binding!!.tvClassName.text = "选择过滤的群聊"
        loadingDialog!!.show()
        binding!!.rlSelect.layoutManager = LinearLayoutManager(this)
        binding!!.rlSelect.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding!!.fabRefresh.visibility = View.VISIBLE
                } else if (dy == 0) {
                    binding!!.fabRefresh.visibility = View.VISIBLE
                } else {
                    binding!!.fabRefresh.visibility = View.GONE
                }
            }
        })
        adapter = FilterAdapter(beanList, this)
        binding!!.rlSelect.adapter = adapter
        adapter!!.setOnItemClickListener(this)
        if (config.filter != "") {
            filterBean = FilterBean()
            var jsonArray: JsonArray =
                parser.parse(config.filter).asJsonArray
            for (user in jsonArray) {
                filterBean = FilterBean()
                filterBean = gson.fromJson(user, FilterBean::class.java)
                beanList.add(filterBean)
            }
            if (config.selectfilter != "") {
                val list: MutableList<FilterSaveBean?> = ArrayList()
                jsonArray =
                    parser.parse(config.selectfilter).asJsonArray
                for (user in jsonArray) {
                    bean = FilterSaveBean()
                    //使用GSON，直接转成Bean对象
                    bean = gson.fromJson(user, FilterSaveBean::class.java)
                    list.add(bean)
                }
                for (i in beanList.indices) {
                    for (j in list.indices) {
                        if (beanList[i]!!.name == list[j]!!.name) {
                            beanList[i]!!.isCheck = true
                        }
                    }
                }
            }
            adapter!!.notifyDataSetChanged()
            loadingDialog!!.dismiss()
        } else {
            startService(Intent(this, GroupChatService::class.java))
        }
        binding!!.ivClassBack.setOnClickListener { v: View? -> finish() }
        binding!!.tvClassAdd.setOnClickListener { v: View? ->
            val list: MutableList<FilterSaveBean> = ArrayList()
            for (i in beanList.indices) {
                if (beanList[i]!!.isCheck) {
                    bean = FilterSaveBean()
                    bean!!.name = beanList[i]!!.name
                    bean!!.displayname = beanList[i]!!.displayname
                    list.add(bean!!)
                }
            }
            config.selectfilter = gson.toJson(list)
            Toast.makeText(this, "添加完成", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding!!.ivClassBack.setOnClickListener { v: View? ->
            beanList.clear()
            //                deleteSingleFile(currApkPath + COPY_WX_DATA_DB);
            startService(Intent(this, GroupChatService::class.java))
            loadingDialog!!.show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getMsg(chatRoommap: Map<String?, String?>) {
        loadingDialog!!.dismiss()
        for ((key, value) in chatRoommap) {
            filterBean = FilterBean()
            filterBean!!.name = key
            filterBean!!.displayname = value
            filterBean!!.isCheck = false
            beanList.add(filterBean)
        }
        config.filter = gson.toJson(beanList)
        if (config.selectfilter != "") {
            val list: MutableList<FilterSaveBean?> = ArrayList()
            val jsonArray: JsonArray =
                parser.parse(config.selectfilter).asJsonArray
            for (user in jsonArray) {
                bean = FilterSaveBean()
                //使用GSON，直接转成Bean对象
                bean = gson.fromJson(user, FilterSaveBean::class.java)
                list.add(bean)
            }
            for (i in beanList.indices) {
                for (j in list.indices) {
                    if (beanList[i]!!.name == list[j]!!.name) {
                        beanList[i]!!.isCheck = true
                    }
                }
            }
        }
        adapter!!.notifyDataSetChanged()
        stopService(Intent(this, GroupChatService::class.java))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getError(msg: MessageEvent) {
        if (msg.message == "error") {
            loadingDialog!!.dismiss()
            stopService(Intent(this, GroupChatService::class.java))
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
        super.onDestroy()
    }

    override fun itemClickListener(v: View, i: Int) {
        if (beanList[i]!!.isCheck) {
            beanList[i]!!.isCheck = false
        } else {
            beanList[i]!!.isCheck = true
        }
        adapter!!.notifyDataSetChanged()
    }

    /**
     * 删除单个文件
     *
     * @param filePathName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private fun deleteSingleFile(`filePath$Name`: String): Boolean {
        val file = File(`filePath$Name`)
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        return if (file.exists() && file.isFile) {
            if (file.delete()) {
                Log.e(
                    "--Method--",
                    "Copy_Delete.deleteSingleFile: 删除单个文件" + `filePath$Name` + "成功！"
                )
                true
            } else {
                Toast.makeText(
                    applicationContext,
                    "删除单个文件" + `filePath$Name` + "失败！",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        } else {
            Toast.makeText(
                applicationContext,
                "删除单个文件失败：" + `filePath$Name` + "不存在！",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }
}
