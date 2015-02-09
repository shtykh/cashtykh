package shtykh.storage.internal;

import shtykh.storage.Storage;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by shtykh on 07/02/15.
 */
public class MemoryStorage<Key, Value extends Serializable> extends HashMap<Key, Value> implements Storage<Key, Value> {
}
