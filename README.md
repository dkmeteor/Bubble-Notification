
# Demo
![Examples list](http://dk-exp.com/wp-content/uploads/2014/08/WaterDrop.gif)


Effect like QQ 5.0.

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


# TODO
The line is not smooth , I'd refine it soon.
Support several different styles.



## License
Copyright (c) 2014 [Dean Ding](http://dk-exp.com)

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)