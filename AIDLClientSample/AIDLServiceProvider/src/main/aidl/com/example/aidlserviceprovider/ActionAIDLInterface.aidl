// ActionAIDLInterface.aidl
package com.example.aidlserviceprovider;

// Declare any non-default types here with import statements

import com.example.aidlserviceprovider.PushResultAIDLInterface;

interface ActionAIDLInterface {

   // int getDeviceActionDetails();
    void register(PushResultAIDLInterface pushResult);

}