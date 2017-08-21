package com.ekincare.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by rakeshk on 05-02-2015.
 */
public  class UserAccountUtility {

    public static String getCurrentUserEmailAccount(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                String domain = possibleEmail.substring(possibleEmail.lastIndexOf("@"));
                if (domain.equals("@gmail.com"))
                    return possibleEmail;

            }
        }
        return null;
    }
}
