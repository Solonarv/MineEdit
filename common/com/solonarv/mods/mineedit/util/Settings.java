package com.solonarv.mods.mineedit.util;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    
    private static final Map<String, Settings> settingsMap = new HashMap<String, Settings>();
    
    private final Map<String, Object> settings = new HashMap<String, Object>();
    
    

    public static Settings getSettingsFor(String name) {
        return settingsMap.get(name);
    }

    public <T> Settings putSetting(String settingName, T value, Class<? super T> declaredClass){
        
        return this;
    }

    public boolean getBooleanSetting(String settingName, boolean _default) {
        Boolean result = getSetting(settingName, Boolean.class);
        if(result == null){
            settings.put(settingName, (Boolean) _default);
            return _default;
        }else{
            return result;
        }
        
    }

    private <T> T getSetting(String settingName, Class<T> targetClass) {
        Object obj = settings.get(settingName);
        if(targetClass.isInstance(obj)){
            return targetClass.cast(obj);
        }
        return null;
    }
}
