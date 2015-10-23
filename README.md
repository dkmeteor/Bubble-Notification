[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Bubble--Notification-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/875)

# Demo

![Examples list](/gif/list_demo1.gif)

![Examples list](/gif/list_demo2.gif)

![Examples list](/gif/heart.gif)

Effect like QQ 5.0 and add an explosion animation.

# How to use

    <com.dk.view.drop.WaterDrop
    	android:id="@+id/drop"
		android:layout_width="25dp"
		android:layout_height="25dp"
		android:gravity="center_vertical" />

	WaterDrop drop = (WaterDrop) convertView.findViewById(R.id.drop);
		drop.setText(String.valueOf(position));
		drop.setOnDragCompeteListener(new OnDragCompeteListener() {

				@Override
				public void onDrag() {
					Toast.makeText(MainActivity.this, "remove:" + position,
							Toast.LENGTH_SHORT).show();
				}
			});


# TODO / UPDATE



- ~~迁移到Android Studio~~

- ~~添加Gif效果支持~~

- 添加爆炸效果Gif (3/n)

- ~~重构代码结构~~

- 提交Jcenter

- 待处理特效 

    ![Examples list](/gif/explosion_all.gif)

    ![Examples list](/gif/energy_all.gif)

    ![Examples list](/gif/smoke_all.gif)

    ![Examples list](/gif/smoke_explosion_all.gif)

    AE渲染, 导出序列帧, 再用PS制作Gif, 过程比较繁琐.没有找到方便的处理脚本 / 工具 / 方法的话,不会制作太多特效, 挑一些有特点的做一下.

- Gif对Alpha支持非常差,渲染出的半透明光晕要么效果几乎没有,要么就是大片白色,未来可能添加 animation-list支持,并直接使用AE导出的序列帧.


## License

    Copyright 2015 Dean Ding

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

---
Developed By


Dean <93440331@qq.com>  

Weibo：http://weibo.com/u/2699012760

Blog: [http://dk-exp.com/](http://dk-exp.com/)

![](https://avatars0.githubusercontent.com/u/5019523?v=3&s=460)