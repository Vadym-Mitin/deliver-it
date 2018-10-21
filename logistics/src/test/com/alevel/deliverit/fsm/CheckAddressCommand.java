package com.alevel.deliverit.fsm;

import com.alevel.deliverit.logistics.fsm.Command;
import com.alevel.deliverit.logistics.fsm.Context;

public class CheckAddressCommand implements Command {
    @Override
    public void perform(Context context) {
        context.isCheckedAddress = true;
    }
}
