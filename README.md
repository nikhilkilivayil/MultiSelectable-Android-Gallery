MultiSelectable-Android-Gallery
===============================

Custom MultiSelectable Gallery for Android

[![Gallery](https://raw.githubusercontent.com/samjerusalem/MultiSelectable-Android-Gallery/master/a.png)]
[![Gallery](https://raw.githubusercontent.com/samjerusalem/MultiSelectable-Android-Gallery/master/b.png)]
[![Gallery](https://raw.githubusercontent.com/samjerusalem/MultiSelectable-Android-Gallery/master/c.png)]

<h3>Usage</h3>
```java
Intent intent = new Intent(this, SamsGallery.class);
intent..putExtra(SamsGallery.GALLERY_TYPE, SamsGallery.IMAGES);
startActivityForResult(intent, REQUEST_GALLERY);
```

for image gallery,
```java
intent..putExtra(SamsGallery.GALLERY_TYPE, SamsGallery.IMAGES);
```
for video gallery,
```java
intent..putExtra(SamsGallery.GALLERY_TYPE, SamsGallery.VIDEOS);
```

and result would be an arraylist of Data objects
```java
ArrayList<Data> datalist = data.getParcelableArrayListExtra(SamsGallery.RESULT);
for(Data imageData: datalist){
	String mediaPath = imageData.getMediaPath();
	String thumbnailId = imageData.getThumbnailId();
}
```
