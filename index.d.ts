declare module 'react-native-adyen-encrypt' {
    interface CardForm {
        cardNumber: string;
        securityCode: string;
        expiryMonth: string;
        expiryYear: string;
    }

    declare class AdyenEncryptor {
        constructor(adyenPublicKey: string);

        encryptCard(cardForm: CardForm);
    }
}
