package com.message.model;

/**
 * Created by Ajay on 31-01-2017.
 */

public class WalletResponse {
    private String status;

    private Wallet wallet;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Wallet getWallet ()
    {
        return wallet;
    }

    public void setWallet (Wallet wallet)
    {
        this.wallet = wallet;
    }

    @Override
    public String toString()
    {
        return "WalletResponse [status = "+status+", wallet = "+wallet+"]";
    }
}
