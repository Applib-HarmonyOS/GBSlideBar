package so.orion.slidebar.slice;

import ohos.agp.utils.Color;
import ohos.global.config.ConfigManager;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.resource.*;
import ohos.global.resource.solidxml.Pattern;
import ohos.global.resource.solidxml.SolidXml;
import ohos.global.resource.solidxml.Theme;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import so.orion.gbslidebar.GBSlideBar;
import so.orion.gbslidebar.GBSlideBarListener;
import so.orion.slidebar.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import java.io.IOException;
import java.util.List;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, "GBSlideBar.class");
    private GBSlideBar gbSlideBar;
    private SlideAdapter mAdapter;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
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
    }

}
