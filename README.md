GBSlideBar
=================

Introduction
------------
HMOS library which handles custom animation for sliding selection toolbar

Source
------------

The code in this repository was inspired from [edanel/GBSlideBar](https://github.com/edanel/GBSlideBar). We are very
 thankful to edanel.

Demo
------------

![animation](https://raw.githubusercontent.com/edanel/GBSlideBar/master/screenshot/preview-480.gif)

Installation
------------

In order to use the library, add the following line to your **root** gradle file:

I) For using GBSlideBar module in sample app, include the source code and add the below
 dependencies in entry/build.gradle to generate hap/support.har.
```
dependencies {
        implementation project(':slidebar')
        implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])
        testImplementation 'junit:junit:4.13'
}
```
II) For using GBSlideBar in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
```
dependencies {
        implementation fileTree(dir: 'libs', include: ['*.har'])
        testImplementation 'junit:junit:4.12'
}
```

Usage
-----

I) Declare Custom GBSlideBar in XML (see xml attributes below for customization):

        <so.orion.slidebar.GBSlideBar
            ohos:id="$+id:gbslidebar"
            ohos:height="100vp"
            ohos:width="match_content"
            ohos:center_in_parent="true"
            app:gbs_anchor_height="25vp"
            app:gbs_anchor_width="25vp"
            app:gbs_background="#e0e0e0"
            app:gbs_paddingBottom="65vp"
            app:gbs_placeholder_width="20vp"
            app:gbs_placeholder_height="20vp"
            app:gbs_paddingLeft="10vp"
            app:gbs_paddingRight="10vp"
            app:gbs_paddingTop="25vp"
            app:gbs_textSize="14fp"
            app:gbs_textColor="#666"/>


II) Usage in java 

        GBSlideBar gbSlideBar;
        SlideAdapter mAdapter;
        
        gbSlideBar= (GBSlideBar) findComponentById(ResourceTable.Id_gbslidebar);
        ResourceManager resourceManager=this.getResourceManager();
        
        mAdapter = new SlideAdapter(MainAbilitySlice.this,resourceManager , new int[]{
                ResourceTable.Graphic_btn_tag_selector,
                ResourceTable.Graphic_btn_more_selector,
                ResourceTable.Graphic_btn_reject_selector
        });
        mAdapter.setTextColor(new Color[]{
                Color.GREEN,
                Color.BLUE,
                Color.RED
        });
        
        gbSlideBar.registerDrawTask();
        gbSlideBar.setAdapter(mAdapter);
        gbSlideBar.setPosition(2);
        gbSlideBar.setOnGbSlideBarListener(position -> {
            HiLog.debug(LABEL_LOG, "position selected", "");
        });
        

