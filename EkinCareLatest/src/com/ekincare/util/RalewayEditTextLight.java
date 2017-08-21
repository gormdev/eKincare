package com.ekincare.util;

import java.io.File;

import com.ekincare.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;


/**
 * Created by rakeshk on 16-01-2015.
 */
public class RalewayEditTextLight extends EditText {

	/*
	 * Permissible values ​​for the "typeface" attribute.
	 */


	private final static int RALEWAY_BOLD = 0;
	private final static int RALEWAY_EXTRA_BOLD = 1;
	private final static int RALEWAY_EXTRA_LIGHT = 2;
	private final static int RALEWAY_HEAVY = 3;
	private final static int RALEWAY_LIGHT = 4;
	private final static int RALEWAY_MEDIUM = 5;
	private final static int RALEWAY_REGULAR = 6;
	private final static int RALEWAY_SEMI_BOLD = 7;
	private final static int RALEWAY_THIN = 8;
	/**
	 * List of created typefaces for later reused.
	 */
	private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(16);

	//    private int textcolors = getResources().getColor(android.R.color.black);

	public RalewayEditTextLight(Context context) {
		super(context);
	}

	public RalewayEditTextLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	public RalewayEditTextLight(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		parseAttributes(context, attrs);
	}

	/**
	 * Parse the attributes.
	 *
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param attrs   The attributes of the XML tag that is inflating the view.
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.RobotoTextView);

		int typefaceValue = values.getInt(R.styleable.RobotoTextView_typeface, 0);
		values.recycle();

//		setTypeface(obtaintTypeface(context, typefaceValue));
		setTypeface(obtaintTypeface(context, 4));
	}

	/**
	 * Obtain typeface.
	 *
	 * @param context       The Context the view is running in, through which it can
	 *                      access the current theme, resources, etc.
	 * @param typefaceValue values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException if unknown `typeface` attribute value.
	 */
	private Typeface obtaintTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
		Typeface typeface = mTypefaces.get(typefaceValue);
		if (typeface == null) {
			typeface = createTypeface(context, typefaceValue);
			mTypefaces.put(typefaceValue, typeface);
		}
		return typeface;
	}

	/**
	 * Create typeface from assets.
	 *
	 * @param context       The Context the view is running in, through which it can
	 *                      access the current theme, resources, etc.
	 * @param typefaceValue values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException if unknown `typeface` attribute value.
	 */
	private Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
		Typeface typeface;
		switch (typefaceValue) {
		case RALEWAY_BOLD:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts" + File.separator + "Roboto-Bold.ttf");
            break;
        case RALEWAY_EXTRA_BOLD:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf");
            break;
        case RALEWAY_EXTRA_LIGHT:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            break;
        case RALEWAY_HEAVY:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            break;
        case RALEWAY_LIGHT:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            break;
        case RALEWAY_MEDIUM:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts" + File.separator + "Roboto-Medium.ttf");
            break;
        case RALEWAY_REGULAR:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            break;
        case RALEWAY_SEMI_BOLD:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
            break;
        case RALEWAY_THIN:
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
            break;
		default:
			throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
		}
		return typeface;
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		InputConnection connection = super.onCreateInputConnection(outAttrs);
		int imeActions = outAttrs.imeOptions&EditorInfo.IME_MASK_ACTION;
		if ((imeActions&EditorInfo.IME_ACTION_DONE) != 0) {
			// clear the existing action
			outAttrs.imeOptions ^= imeActions;
			// set the DONE action
			outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
		}
		if ((outAttrs.imeOptions&EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
			outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
		}
		return connection;
	}

	//    @Override
	//    public void setTextColor(ColorStateList colors) {
	//    	super.setTextColor(textcolors);
	//    }

}
