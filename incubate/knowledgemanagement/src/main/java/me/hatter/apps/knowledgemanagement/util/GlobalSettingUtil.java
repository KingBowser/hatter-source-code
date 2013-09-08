package me.hatter.apps.knowledgemanagement.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.apps.knowledgemanagement.entity.GlobalSetting;
import me.hatter.tools.commons.collection.CollectionUtil;
import me.hatter.tools.jsspserver.action.DatabaseAction;

public class GlobalSettingUtil {

    public static enum SettingKey {
        TARGET_PATH,

        SIDEBAR_NAME,

        ;
    }

    public static final Map<String, String> map = new HashMap<String, String>();
    static {
        refreshMap();
    }

    synchronized public static void refreshMap() {
        map.clear();
        List<GlobalSetting> settings = DatabaseAction.DAO.listObjects(GlobalSetting.class);
        Map<String, String> _map;
        _map = CollectionUtil.toMap(settings, new CollectionUtil.KeyGetter<GlobalSetting, String>() {

            public String getKey(GlobalSetting object) {
                return object.getName();
            }
        }, new CollectionUtil.Transformer<GlobalSetting, String>() {

            public String transform(GlobalSetting object) {
                return object.getValue();
            }
        });
        map.putAll(_map);
    }

    synchronized public static Map<String, String> cloneMap() {
        Map<String, String> _map = new HashMap<String, String>();
        _map.putAll(map); // low level clone Map<String, String>
        return _map;
    }

    synchronized public static String getValue(SettingKey key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        return map.get(key.name());
    }

    synchronized public static String getValueByStrKey(String key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        return map.get(key);
    }

    synchronized public static void setValue(SettingKey key, String value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null.");
        GlobalSetting setting = new GlobalSetting();
        setting.setName(key.name());
        setting.setValue(value);
        DatabaseAction.DAO.insertOrUpdateObject(setting);
        map.put(key.name(), value);
    }
}
