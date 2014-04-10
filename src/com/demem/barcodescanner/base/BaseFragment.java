package com.demem.barcodescanner.base;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	protected Context _context;

	public void setContext(Context context)
    {
    	_context = context;
    }
}
