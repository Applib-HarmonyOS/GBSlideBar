package so.orion.gbslidebar;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import so.orion.gbslidebar.slice.MainAbilitySlice;

/**
 * MainAbility.
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
