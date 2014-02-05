package me.hatter.tools.commons.args;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.hatter.tools.commons.args.util.KList;
import me.hatter.tools.commons.args.util.KMap;
import me.hatter.tools.commons.args.util.KSet;

public class KArgs {

    KList       args      = new KList();
    KSet        flags     = new KSet();
    KMap        keyvalues = new KMap();
    Set<String> fset      = new HashSet<String>();

    String[]    _args     = null;
    KSet        _keys     = null;

    public void addFSet(String... fkeys) {
        if ((fkeys != null) && (fkeys.length > 0)) {
            for (String fk : fkeys) {
                fset.add(fk);
            }
        }
    }

    public String[] args() {
        synchronized (this) {
            if (_args == null) {
                _args = args.toArray(new String[0]);
            }
        }
        return _args;
    }

    public KSet flags() {
        return flags;
    }

    public KSet keys() {
        synchronized (this) {
            if (_keys == null) {
                _keys = new KSet(keyvalues.keySet());
            }
        }
        return _keys;
    }

    public String kvalue(String key) {
        List<String> vs = keyvalues.get(key);
        return ((vs == null) || (vs.size() == 0)) ? null : vs.get(0);
    }

    public String kvalueAny(String... keys) {
        if ((keys != null) && (keys.length > 0)) {
            for (String key : keys) {
                List<String> vs = keyvalues.get(key);
                String val = ((vs == null) || (vs.size() == 0)) ? null : vs.get(0);
                if (val != null) {
                    return val;
                }
            }
        }
        return null;
    }

    public String kvalue(String key, String defval) {
        List<String> vs = keyvalues.get(key);
        return ((vs == null) || (vs.size() == 0)) ? defval : vs.get(0);
    }

    public List<String> kvalues(String key) {
        List<String> vs = keyvalues.get(key);
        return (vs == null) ? null : Collections.unmodifiableList(vs);
    }

    public List<String> kvalues(String key, List<String> defval) {
        List<String> vs = keyvalues.get(key);
        return (vs == null) ? defval : Collections.unmodifiableList(vs);
    }
}
