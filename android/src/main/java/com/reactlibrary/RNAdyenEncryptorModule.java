package com.reactlibrary;

import com.adyen.checkout.cse.UnencryptedCard;
import com.adyen.checkout.cse.CardEncrypter;
import com.adyen.checkout.cse.EncryptedCard;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNAdyenEncryptorModule extends ReactContextBaseJavaModule {

    private final String SUCCESS_CALLBACK = "AdyenCardEncryptedSuccess";
    private final String ERROR_CALLBACK = "AdyenCardEncryptedError";
    private final ReactApplicationContext reactContext;

    public RNAdyenEncryptorModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AdyenEncryptor";
    }

    @ReactMethod
    public void encryptWithData(ReadableMap cardData) throws Exception {
        String cardNumber = cardData.getString("cardNumber");
        String securityCode = cardData.getString("securityCode");
        String expiryMonth = cardData.getString("expiryMonth");
        String expiryYear = cardData.getString("expiryYear");
        String publicKey = cardData.getString("publicKey");
        WritableNativeMap encryptedCardMap = new WritableNativeMap();

        try {
            UnencryptedCard card = new UnencryptedCard.Builder()
                    .setNumber(cardNumber)
                    .setCvc(securityCode)
                    .setExpiryMonth(expiryMonth)
                    .setExpiryYear(expiryYear)
                    .build();
            EncryptedCard encryptedCard = CardEncrypter.encryptFields(card, publicKey);

            encryptedCardMap.putString("encryptedCardNumber", encryptedCard.getEncryptedCardNumber());
            encryptedCardMap.putString("encryptedSecurityCode", encryptedCard.getEncryptedSecurityCode());
            encryptedCardMap.putString("encryptedExpiryMonth", encryptedCard.getEncryptedExpiryMonth());
            encryptedCardMap.putString("encryptedExpiryYear", encryptedCard.getEncryptedExpiryYear());
        } catch (Exception e) {
            encryptedCardMap.putString("error Custom", e.toString());
            this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(ERROR_CALLBACK,
                    encryptedCardMap);
        }

        this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(SUCCESS_CALLBACK,
                encryptedCardMap);
    }

}
