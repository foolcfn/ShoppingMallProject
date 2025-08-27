package com.cfl.network.httphelper.monitor.ui

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cfl.network.httphelper.monitor.db.MonitorDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


object MonitorNotification {

	//定义一个协程作用域
	private var monitorObserver:Job? = null

	fun init(context: Application){
		//1.创建渠道

		var channelId  = "Monitor"
		var channelName = "Monitor Http"
		var channelDescription = "Automatically record http request"
		var notificationTitle = "Monitor Http"
		val channel = NotificationChannelCompat.Builder(
			channelId,
			NotificationManagerCompat.IMPORTANCE_DEFAULT	//默认优先级
		).setName(channelName)
			.setDescription(channelDescription)
			.setSound(null,null)	//禁用声音
			.setLightsEnabled(false)				//禁用指示灯
			.setVibrationEnabled(true)			//开启震动
			.setShowBadge(true)
			.build()
		//不是Activity无法拿到系统服务，只能通过Notification去拿到系统服务 -》 通过系统服务创建渠道
		NotificationManagerCompat.from(context).createNotificationChannel(channel)
		//拿到通知管理者
		val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		//开启拿到数据
		monitorObserver?.cancel()
		monitorObserver = GlobalScope.launch(context = Dispatchers.Default) {

			var queryFlow = MonitorDatabase.instance.monitorDao().queryMonitors(limit = 5)		//减小开支，只取5条数据
			queryFlow.map {
				it.map { monitor ->
					monitor.notificationText
				}
			}.collect {
				//拿到数据，collect收集时就触发这里的逻辑
				show(context = context,
					notificationManager = notificationManager,
					channelId = channelId,
					notificationTitle = notificationTitle,
					monitorList = it)
			}
		}
	}

	private fun show(
		context: Context,
		notificationManager: NotificationManager,
		channelId: String,
		notificationTitle: String,
		monitorList: List<String>
	){
		//为消息取个ID号
		val notificationId = 0x20250826
		if (monitorList.isEmpty()){
			//数据为空时取消Id号
			notificationManager.cancel(notificationId)
		} else{
			//在此处建立通知栏样式
			val inboxStyle = NotificationCompat.InboxStyle()
			monitorList.forEach {
				//添加可折叠文本
				inboxStyle.addLine(it)
			}

			//数据不为空 创建一条消息并从渠道发送 创建消息时就要创建渠道
			val notification = NotificationCompat.Builder(context,channelId)
				.setContentText(monitorList.first())
				.setContentTitle(notificationTitle)
				.setSmallIcon(com.cfl.network.R.drawable.monitor_notification_icon)
				.setAutoCancel(false)
				.setOnlyAlertOnce(true)
				.setContentIntent(getContentIntent(context))		//建立一个跳转的通知
				.setStyle(inboxStyle)
				.build()
			//由notificationManager创建渠道同时由Manager发送消息
			notificationManager.notify(notificationId,notification)
		}

	}

	private fun getContentIntent(context: Context): PendingIntent? {
		val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE		//二进制按位或 表示同时启动这两种行为
		} else{
			PendingIntent.FLAG_UPDATE_CURRENT
		}

		var intent = Intent(context, MonitorActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		//请求码
		return PendingIntent.getActivity(
			context				//依赖的环境
			,100	//区分不同的Pending
			,intent				//意图
			,flag			//控制Pending行为的标志位
			)
	}


}