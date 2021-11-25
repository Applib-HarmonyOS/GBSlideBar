package so.orion.slidebar.slice;

import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementContainer;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.global.resource.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import so.orion.gbslidebar.GBSlideBarAdapter;
import so.orion.gbslidebar.ResourceTable;
import so.orion.gbslidebar.Utils.ResUtil;

import java.io.IOException;
import java.util.Optional;

public class SlideAdapter implements GBSlideBarAdapter {
    protected StateElement[] mItems;
    protected String[] content = new String[]{"Tag","More","Reject"};
    protected Color[] textColor;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, "GBSlideBar.class");

    public SlideAdapter(Context context, ResourceManager resourceManager, int[] items)  {
        int size = items.length;
        mItems=new StateElement[size];
        Element drawable=null;
        for (int i = 0; i < size; i++) {
            drawable = ElementScatter.getInstance(context).parse(items[i]);

            if (drawable instanceof StateElement) {
                mItems[i] = (StateElement) drawable;
            } else {
                mItems[i] = new StateElement();
                mItems[i].addState(new int[] {}, drawable);
            }
        }
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public String getText(int position) {
        return content[position];
    }

    @Override
    public StateElement getItem(int position) {
        return mItems[position];
    }

    @Override
    public Color getTextColor(int position) {
        return textColor[position];
    }
    public void setTextColor(Color[] color){
        textColor = color;
    }
}
