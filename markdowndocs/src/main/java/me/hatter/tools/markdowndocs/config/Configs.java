package me.hatter.tools.markdowndocs.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import me.hatter.tools.commons.reflect.ReflectUtil;

public class Configs {

    public static final Config DEFAULT = new Config();
    static {
        DEFAULT.setTitle("Sample Page");
        DEFAULT.setFavicon114("/assets/img/apple-touch-icon-144-precomposed.png");
        DEFAULT.setFavicon("/assets/img/favicon.png");
        DEFAULT.setHeadTitle("Sample Head Title");
        DEFAULT.setImgType("png");
        DEFAULT.setSmallSize(200);
        DEFAULT.setBigSize(1200);
        DEFAULT.setQuality(0.95);
    }

    public static Config mergeConfig(Config... confings) {
        Config conf = new Config();
        List<Field> fields = ReflectUtil.getDeclaredFields(Config.class);
        FIELD_LOOP: for (Field field : fields) {
            if ((!Modifier.isStatic(field.getModifiers())) && (!Modifier.isFinal(field.getModifiers()))) {
                field.setAccessible(true);
                for (Config config : confings) {
                    try {
                        Object o = field.get(config);
                        if (o != null) {
                            field.set(conf, o);
                            continue FIELD_LOOP;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return conf;
    }
}
