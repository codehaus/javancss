package net.tds.openwave.ldap.attributes;

public enum TempBooleanAttributes implements BooleanAttribute {
    mailInterManagerSsl("1","0"),
    mailSmtpAccess("1","0"),
    mailSmtpSslAccess("1","0"),
    mailSmtpAuth("1","0"),
    mailMtaFilter("1","0"),
    mailQuotaBounceNotify("1","0"),
    mailForwarding("1","0"),
    mailSelfCare("1","0"),
    mailSelfCareSsl("1","0"),
    mailInterManager("1","0"),
    mailWebMailUseSignature("1","0"),
    mailWebMailConfirmDelete("1","0"),
    mailMtaFilterPerUser("1","0"),
    mailParentalControl("1","0"),
    mailDeliveryAccess("1","0"),
    mailFutureDeliveryEnabled("1","0"),
    mailAutoSave("1","0"),
    mailAutoSignature("1","0"),
    mailAutoVcard("1","0"),
    mailIncludeOriginal("1","0"),
    mailPopUnifyEnabled("1","0"),
    listVerifySubscribeRequest("TRUE","FALSE"),
    listVerifyUnsubscribeRequest("TRUE","FALSE"),
    listDetectRequest("TRUE","FALSE"),
    listRemoveXHeaders("TRUE","FALSE"),
    listStatisticsNotification("TRUE","FALSE"),
    listBounceNotification("TRUE","FALSE"),
    listOverLimitNotification("TRUE","FALSE"),
    listDynamic("TRUE","FALSE");

    private final String name = toString();
    private final String trueValue;
    private final String falseValue;

    private TempBooleanAttributes(String trueValue, String falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    public String getName() {
        return name;
    }

    public boolean isSingleValued() {
        return true;
    }

    public String getTrueString() {
        return trueValue;
    }

    public String getFalseString() {
        return falseValue;
    }

    public boolean equals(BooleanAttribute attribute) {
        return this == attribute;
    }
}
