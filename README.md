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

gallery can be either video or image, and can be opt out by passing gallery type,<br><br>
for images
```java
intent..putExtra(SamsGallery.GALLERY_TYPE, SamsGallery.IMAGES);
```
for videos
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
