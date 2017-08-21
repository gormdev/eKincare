package com.message.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ekincare.R;
import com.ekincare.ui.custom.CustomSeekBar;
import com.ekincare.ui.custom.ProgressItem;
import com.ekincare.util.CircularSeekBar;

import java.util.ArrayList;

public class IncomingMemberProfileViewHolder extends MessageViewHolder {

    public CustomSeekBar seekbar;
    public SeekBar seekbarHealthScore;
    public SeekBar seekbarOptimalHealthScore;
    public TextView textViewOrigionalHs;
    public TextView textViewOptimalHs;
    public FrameLayout frameLayoutTextHealthScore;
    public FrameLayout frameLayoutTextOptimalHealthScore;
    public TextView textViewProfileText,textViewViewInsight,textProgressLimitRange;

    public IncomingMemberProfileViewHolder(View itemView)
    {
        super(itemView);

        textViewProfileText = (TextView) itemView.findViewById(R.id.text_view_profile_text);
        textViewViewInsight = (TextView) itemView.findViewById(R.id.text_view_insight);
        textProgressLimitRange=(TextView)itemView.findViewById(R.id.progress_limit_show);

        seekbar = (CustomSeekBar) itemView.findViewById(R.id.seekBar0);
        seekbarHealthScore = (SeekBar) itemView.findViewById(R.id.seekbar_origional_hs);
        //seekbarHealthScore.setMax(100);

        seekbarOptimalHealthScore = (SeekBar) itemView.findViewById(R.id.seekbar_optimal_hs);
        //seekbarOptimalHealthScore.setMax(100);

        textViewOrigionalHs = (TextView) itemView.findViewById(R.id.text_original_hs_seek);
        textViewOptimalHs = (TextView) itemView.findViewById(R.id.text_view_optimal_hs);

        frameLayoutTextHealthScore = (FrameLayout) itemView.findViewById(R.id.text_origional_hs_container);
        frameLayoutTextOptimalHealthScore = (FrameLayout) itemView.findViewById(R.id.optimal_hs_container);
    }
}
