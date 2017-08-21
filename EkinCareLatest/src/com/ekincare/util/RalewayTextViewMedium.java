package com.ekincare.util;


import com.ekincare.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

public class RalewayTextViewMedium extends TextView {
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
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(9);

	public RalewayTextViewMedium(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor that is called when inflating a view from XML. This is called
	 * when a view is being constructed from an XML file, supplying attributes
	 * that were specified in the XML file. This version uses a default style of
	 * 0, so the only attribute values applied are those in the Context's Theme
	 * and the given AttributeSet.
	 * <p/>
	 * <p/>
	 * The method onFinishInflate() will be called after all children have been
	 * added.
	 *
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param attrs
	 *            The attributes of the XML tag that is inflating the view.
	 * @see #RobotoTextView(Context, AttributeSet, int)
	 */
	public RalewayTextViewMedium(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	/**
	 * Parse the attributes.
	 *
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param attrs
	 *            The attributes of the XML tag that is inflating the view.
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs,
				R.styleable.RalewayTextViewMedium);

		int typefaceValue = values.getInt(R.styleable.RalewayTextViewMedium_ralewaytypeface,0);
		values.recycle();

//		System.out.println("------typeface "+typefaceValue);
//		setTypeface(obtaintTypeface(context, typefaceValue));
		setTypeface(obtaintTypeface(context, 5));

	}

	/**
	 * Obtain typeface.
	 *
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param typefaceValue
	 *            values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException
	 *             if unknown `typeface` attribute value.
	 */
	private Typeface obtaintTypeface(Context context, int typefaceValue)
			throws IllegalArgumentException {
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
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
                break;
            case RALEWAY_EXTRA_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
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
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
                break;
            case RALEWAY_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                break;
            case RALEWAY_SEMI_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
                break;

            case RALEWAY_THIN:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
                break;
            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }

}
