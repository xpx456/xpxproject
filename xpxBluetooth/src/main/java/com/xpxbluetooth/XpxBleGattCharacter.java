package com.xpxbluetooth;

import com.inuker.bluetooth.library.model.BleGattCharacter;

import java.util.UUID;

public class XpxBleGattCharacter {

    public UUID service;
    public BleGattCharacter bleGattCharacter;

    public XpxBleGattCharacter(UUID service,BleGattCharacter bleGattCharacter)
    {
        this.service = service;
        this.bleGattCharacter = bleGattCharacter;
    }
}
