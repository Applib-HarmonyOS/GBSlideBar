package so.orion.gbslidebar.slice;

import ohos.agp.utils.Color;
import ohos.global.resource.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import so.orion.slidebar.GBSlideBar;
import so.orion.gbslidebar.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, "GBSlideBar.class");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        GBSlideBar gbSlideBar= (GBSlideBar) findComponentById(ResourceTable.Id_gbslidebar);
        ResourceManager resourceManager=this.getResourceManager();
        SlideAdapter mAdapter = new SlideAdapter(MainAbilitySlice.this,resourceManager , new int[]{
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
        gbSlideBar.setOnGbSlideBarListener(position -> HiLog.debug(LABEL_LOG, "position selected", ""));
    }

}
