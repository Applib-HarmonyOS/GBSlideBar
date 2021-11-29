package so.orion.slidebar;

import ohos.agp.components.element.StateElement;
import ohos.agp.utils.Color;

public interface GBSlideBarAdapter {
    int getCount();
    String getText(int position);
    StateElement getItem(int position);
    Color getTextColor(int position);
}
