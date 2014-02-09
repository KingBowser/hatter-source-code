package me.hatter.tools.markdowndocs.assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.hatter.tools.markdowndocs.template.ParameterParser;

public class Assets {

    public static Map<String, Asset> assetMap = new HashMap<String, Asset>();

    static {
        addAsset(makeBSDocAsset());
    }

    public static Asset getGlobalAsset() {
        return getAsset(ParameterParser.getGlobalParamter().getTemplate());
    }

    public static Asset getAsset(String template) {
        Asset asset = assetMap.get(template);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found for template: " + template);
        }
        return asset;
    }

    private static void addAsset(Asset asset) {
        assetMap.put(asset.getTemplate(), asset);
    }

    private static Asset makeBSDocAsset() {
        List<String> resources = new ArrayList<String>();
        resources.add("css/bootstrap.min.css");
        resources.add("css/docs.css");
        resources.add("css/pygments-manni.css");

        resources.add("js/application.js");
        resources.add("js/bootstrap.js");
        resources.add("js/holder.js");
        resources.add("js/html5shiv.js");
        resources.add("js/ie8-responsive-file-warning.js");
        resources.add("js/jquery.min.js");
        resources.add("js/respond.min.js");

        resources.add("img/apple-touch-icon-144-precomposed.png");
        resources.add("img/favicon.png");

        resources.add("prettify/prettify.css");
        resources.add("prettify/prettify.js");
        return new Asset("bsdocs", resources);
    }
}
