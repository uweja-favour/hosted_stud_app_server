package com.xapps.studentapplication.model


enum class NotificationType(val title: String, val defaultMessage: String) {

    SYSTEM(
        title = "System Update",
        defaultMessage = "Important system notification. Please check the details."
    ),

    SIGN_UP(
        title = "Welcome!",
        defaultMessage = "Thank you for signing up. We’re excited to have you on board!"
    ),

    BACK_UP_SUCCESSFUL(
        title = "Back up successful",
        defaultMessage = "Your data has been backed up successfully."
    ),

    SUB_UPDATE(
        title = "Subscription Update",
        defaultMessage = "Your subscription details have been updated."
    ),

    SUB_RENEWAL(
        title = "Subscription Renewed",
        defaultMessage = "Your subscription has been renewed successfully."
    ),

    SUB_ACTIVATED(
        title = "Subscription Activated",
        defaultMessage = "Your subscription has been activated successfully."
    ),

    SUB_CANCELLATION(
        title = "Subscription Cancelled",
        defaultMessage = "Your subscription has been cancelled. We’re sorry to see you go."
    ),

    SIGN_IN(
        title = "Welcome Back",
        defaultMessage = "You have signed in successfully."
    ),

    ALERT(
        title = "Urgent Alert",
        defaultMessage = "Please review this alert immediately."
    ),

    INFO(
        title = "Information",
        defaultMessage = "Here’s some information for you."
    ),

    PROMOTION(
        title = "Special Promotion",
        defaultMessage = "Check out this exclusive offer just for you!"
    );

}
