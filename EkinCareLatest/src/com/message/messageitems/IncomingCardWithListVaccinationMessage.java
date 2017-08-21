package com.message.messageitems;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.message.custominterface.CardWithListButtonClickEvent;
import com.message.custominterface.HorizontalCardListItemClickEvent;
import com.message.custominterface.SchemaClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.TestComponentData;
import com.message.model.VaccaniationDataModel;
import com.message.viewholder.IncomingCardWithListViewHolder;
import com.message.viewholder.IncomingCardWithVaccaniationViewHolder;
import com.message.viewholder.MessageViewHolder;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.Immunization;

import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by RaviTejaN on 06-12-2016.
 */

public class IncomingCardWithListVaccinationMessage extends MessageItem {

    VaccaniationDataModel mTestList;
    private Context context;
    //private Adapter adapter;
    SchemaClickEvent schemaClickEvent;
    private CardWithListButtonClickEvent cardWithListButtonClickEvent;
    HorizontalCardListItemClickEvent horizontalCardListItemClickEvent;
    private CardViewModel cardViewModel;
    String date_of_birth;

    public IncomingCardWithListVaccinationMessage(Context context, VaccaniationDataModel list, CardWithListButtonClickEvent cardWithListButtonClickEvent, CardViewModel cardViewModel, String date_of_birth,SchemaClickEvent schemaClickEvent,HorizontalCardListItemClickEvent horizontalCardListItemClickEvent)
    {
        super();
        this.context = context;
        this.mTestList = list;
       // adapter = new Adapter();
        this.schemaClickEvent = schemaClickEvent;
        this.cardWithListButtonClickEvent = cardWithListButtonClickEvent;
        this.horizontalCardListItemClickEvent =horizontalCardListItemClickEvent;
        this.cardViewModel=cardViewModel;
        this.date_of_birth=date_of_birth;
        //System.out.println("IncomingCardWithListVaccinationMessage.IncomingCardWithListVaccinationMessage list"+list.size());
    }



    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        System.out.println("IncomingCardWithListVaccinationMessage.buildMessageItem=========="+mTestList.getName()+""+cardViewModel.getSubTitle());
        System.out.println("IncomingCardWithListVaccinationMessage.buildMessageItem =="+ (messageViewHolder instanceof IncomingCardWithVaccaniationViewHolder));
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardWithVaccaniationViewHolder) {
            System.out.println("IncomingCardWithListVaccinationMessage.buildMessageItem");
            final IncomingCardWithVaccaniationViewHolder incomingcardwithvaccaniationviewholder = (IncomingCardWithVaccaniationViewHolder) messageViewHolder;
            incomingcardwithvaccaniationviewholder.vaccaniationName.setText(mTestList.getName());
            incomingcardwithvaccaniationviewholder.viewmoreButton.setText(cardViewModel.getButtonText1());

            incomingcardwithvaccaniationviewholder.viewmoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSchemaPresent(cardViewModel.getButtonAction1())){
                        String id = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("@")+1,cardViewModel.getButtonAction1().indexOf("/"));
                        String transitionScreen = cardViewModel.getButtonAction1().substring(cardViewModel.getButtonAction1().indexOf("/")+1);

                        schemaClickEvent.onSchemaItemClick(id,transitionScreen);
                    }else{
                        horizontalCardListItemClickEvent.onHorizontalCardListItemClick(cardViewModel.getButtonAction1());
                    }
                }
            });

            incomingcardwithvaccaniationviewholder.vaccnationInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog( mTestList.getName(),mTestList.getBenefits());

                }
            });
           String lastDoseValue = "";
            int currentDoses = 0;
            int TotalDoses = 0;
            if (mTestList.getDose()!= null
                    && !mTestList.getDose().equalsIgnoreCase("")) {
                TotalDoses = Integer.parseInt(mTestList.getDose().substring(0, 1));
            }
            if (mTestList.getCurrent_doses() != null
                    && !mTestList.getCurrent_doses().equalsIgnoreCase("")) {
                currentDoses = Integer.parseInt(mTestList.getCurrent_doses().substring(0, 1));
            }

            VaccaniationDataModel.scheduleData newScheduleData = mTestList.getSchedule();
            System.out.println("IncomingCardWithListVaccinationMessage.buildMessageItem======"+mTestList.getSchedule());
            if (newScheduleData != null) {
                if (newScheduleData.getOne() != null && !newScheduleData.getOne().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getOne().toString();
                }
                if (newScheduleData.getTwo() != null && !newScheduleData.getTwo().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getTwo().toString();
                }
                if (newScheduleData.getThree() != null && !newScheduleData.getThree().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getThree().toString();
                }
                if (newScheduleData.getFour() != null && !newScheduleData.getFour().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getFour().toString();
                }
                if (newScheduleData.getFive() != null && !newScheduleData.getFive().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getFive().toString();
                }
                if (newScheduleData.getSix() != null && !newScheduleData.getSix().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getSix().toString();
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long BirthTimeInMillisecond = 0;
            Date birthDate = null;
            try {
                if ( date_of_birth != null
                        && !date_of_birth.equalsIgnoreCase(""))
                    birthDate = format.parse(date_of_birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar birthCalendar = Calendar.getInstance();
            if (birthDate != null) {
                birthCalendar.setTime(birthDate);
            }
            BirthTimeInMillisecond = birthCalendar.getTimeInMillis();

            int minageTime = 0;
            long minageMilisecond = 0;
            long finalMilisecond = 0;
            String dueDateText = "";
            if (currentDoses == 0) {
                if (mTestList.getMinage() != null) {
                    minageTime = Integer.parseInt(mTestList.getMinage());
                    minageMilisecond = TimeUnit.DAYS.toMillis(minageTime * 7);

                    finalMilisecond = BirthTimeInMillisecond + minageMilisecond;
                }
            } else {
                if (mTestList.getCreated_at() != null) {
                    Date createdDate = null;
                    try {
                        createdDate = format.parse(mTestList.getCreated_at());
                        // System.out.println("-----created date "+createdDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar creatCalendar = Calendar.getInstance();
                    if (createdDate != null) {
                        creatCalendar.setTime(createdDate);
                    }
                    minageMilisecond = creatCalendar.getTimeInMillis();

                    long dueTimeMillisecond = 0;
                    int DueMonth = 0;
                    if (lastDoseValue != null && !lastDoseValue.equalsIgnoreCase("")) {
                        try {
                            DueMonth = Integer.parseInt(lastDoseValue);
                        } catch (NumberFormatException e) {
                        }
                        if (DueMonth > 0) {
                            dueTimeMillisecond = TimeUnit.DAYS.toMillis(DueMonth * 7);
                        }
                    }

                    finalMilisecond = dueTimeMillisecond + minageMilisecond;
                }
            }

            // format.setTimeZone(TimeZone.getTimeZone("IST"));
            // strFirstDate = format.format(currentDate);
            // long FinalTimeInmilli = BirthTimeInMillisecond +
            // timeInMillisecond;
            long currentDate = System.currentTimeMillis();
            dueDateText = getDifferentTime(finalMilisecond, currentDate);

            incomingcardwithvaccaniationviewholder.vacc1.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.vacc2.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.vacc3.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.vacc4.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.vacc5.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.vacc6.setVisibility(View.INVISIBLE);

            incomingcardwithvaccaniationviewholder.imgLable1.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.imgLable2.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.imgLable3.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.imgLable4.setVisibility(View.INVISIBLE);
            incomingcardwithvaccaniationviewholder.imgLable5.setVisibility(View.INVISIBLE);

            incomingcardwithvaccaniationviewholder.vaccaniationOutOf.setText(currentDoses + "/" + TotalDoses);

            if (currentDoses == TotalDoses) {
                // holder.mTextTime.setVisibility(View.GONE);
                incomingcardwithvaccaniationviewholder.vaccaniationTime.setText("All doses are complete");
            } else {
                // holder.mTextTime.setVisibility(View.VISIBLE);

                if (dueDateText.contains("-")) {
                    dueDateText = dueDateText.replaceAll("-", "");
                    incomingcardwithvaccaniationviewholder.vaccaniationTime.setTextColor(context.getResources().getColor(R.color.red));
                    incomingcardwithvaccaniationviewholder.vaccaniationTime.setText(" Vaccination is due ");
                } else {
                    incomingcardwithvaccaniationviewholder.vaccaniationTime.setTextColor(context.getResources().getColor(R.color.themeColor));
                    incomingcardwithvaccaniationviewholder.vaccaniationTime.setText(" Vaccination is due" + dueDateText);
                }
            }


            if (currentDoses > 0) {
                incomingcardwithvaccaniationviewholder.vacc1.setImageResource(R.drawable.check);
                if (currentDoses > 1) {
                    incomingcardwithvaccaniationviewholder.vacc2.setImageResource(R.drawable.check);
                    if (currentDoses > 2) {
                        incomingcardwithvaccaniationviewholder.vacc3.setImageResource(R.drawable.check);
                        if (currentDoses > 3) {
                            incomingcardwithvaccaniationviewholder.vacc4.setImageResource(R.drawable.check);
                            if (currentDoses > 4) {
                                incomingcardwithvaccaniationviewholder.vacc5.setImageResource(R.drawable.check);
                                if (currentDoses > 5) {
                                    incomingcardwithvaccaniationviewholder.vacc6.setImageResource(R.drawable.check);
                                } else {
                                    incomingcardwithvaccaniationviewholder.vacc6.setImageResource(R.drawable.update);
                                }
                            } else {
                                incomingcardwithvaccaniationviewholder.vacc5.setImageResource(R.drawable.update);
                            }
                        } else {
                            incomingcardwithvaccaniationviewholder.vacc4.setImageResource(R.drawable.update);
                        }
                    } else {
                        incomingcardwithvaccaniationviewholder.vacc3.setImageResource(R.drawable.update);
                    }
                } else {
                    incomingcardwithvaccaniationviewholder.vacc2.setImageResource(R.drawable.update);
                }
            } else {
                incomingcardwithvaccaniationviewholder.vacc1.setImageResource(R.drawable.update);
            }

            if (TotalDoses > 0) {
                incomingcardwithvaccaniationviewholder.vacc1.setVisibility(View.VISIBLE);
                if (TotalDoses > 1) {
                    incomingcardwithvaccaniationviewholder.vacc2.setVisibility(View.VISIBLE);
                    incomingcardwithvaccaniationviewholder.imgLable1.setVisibility(View.VISIBLE);
                    if (TotalDoses > 2) {
                        incomingcardwithvaccaniationviewholder.vacc3.setVisibility(View.VISIBLE);
                        incomingcardwithvaccaniationviewholder.imgLable2.setVisibility(View.VISIBLE);
                        if (TotalDoses > 3) {
                            incomingcardwithvaccaniationviewholder.vacc4.setVisibility(View.VISIBLE);
                            incomingcardwithvaccaniationviewholder.imgLable3.setVisibility(View.VISIBLE);
                            if (TotalDoses > 4) {
                                incomingcardwithvaccaniationviewholder.vacc5.setVisibility(View.VISIBLE);
                                incomingcardwithvaccaniationviewholder.imgLable4.setVisibility(View.VISIBLE);
                                if (TotalDoses > 5) {
                                    incomingcardwithvaccaniationviewholder.vacc6.setVisibility(View.VISIBLE);
                                    incomingcardwithvaccaniationviewholder.imgLable5.setVisibility(View.VISIBLE);
                                } else {
                                    incomingcardwithvaccaniationviewholder.vacc6.setVisibility(View.INVISIBLE);
                                    incomingcardwithvaccaniationviewholder.imgLable5.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                incomingcardwithvaccaniationviewholder.vacc5.setVisibility(View.INVISIBLE);
                                incomingcardwithvaccaniationviewholder.imgLable4.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            incomingcardwithvaccaniationviewholder.vacc4.setVisibility(View.INVISIBLE);
                            incomingcardwithvaccaniationviewholder.imgLable3.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        incomingcardwithvaccaniationviewholder.vacc3.setVisibility(View.INVISIBLE);
                        incomingcardwithvaccaniationviewholder.imgLable2.setVisibility(View.INVISIBLE);
                    }
                } else {
                    incomingcardwithvaccaniationviewholder.vacc2.setVisibility(View.INVISIBLE);
                    incomingcardwithvaccaniationviewholder.imgLable1.setVisibility(View.INVISIBLE);
                }
            } else {
                incomingcardwithvaccaniationviewholder.vacc1.setVisibility(View.INVISIBLE);
            }





        }
    }

    public String getDifferentTime(long firstTime, long secondTime) {
        boolean isGraterThanOne = false;
        if (secondTime > firstTime) {
            return "-";
        }
        String returnDate = "";
        // long currentDate = System.currentTimeMillis();
        String strFirstDate = "";
        String strSecondDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // format.setTimeZone(TimeZone.getTimeZone("IST"));
        strFirstDate = format.format(firstTime);
        strSecondDate = format.format(secondTime);
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format.parse(strFirstDate);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Period p = new Period(secondDate.getTime(), firstDate.getTime());
        int diffYear = p.getYears();
        int diffMonth = p.getMonths();
        int diffDay = p.getDays();
        int hours = p.getHours();
        int minutes = p.getMinutes();

        if (diffYear > 0) {
            isGraterThanOne = true;
            if (diffYear > 1) {
                returnDate = " in" + " " + diffYear + " Years ";
            } else {
                returnDate = " in" + " " + diffYear + " Year ";
            }
        } else if (diffYear < 0) {
            isGraterThanOne = true;
            if (diffYear < -1) {
                returnDate = " in" + " " + diffYear + " Years ";
            } else {
                returnDate = " in" + " " + diffYear + " Year ";
            }
        } else if (diffMonth > 0) {
            if (diffMonth > 0) {
                if (diffMonth > 1) {
                    returnDate = returnDate + " in" + " " + diffMonth + " Months ";
                } else {
                    returnDate = returnDate + " in" + " " + diffMonth + " Month ";
                }
            }
        } else if (diffDay == 0) {
            returnDate = " " + "today";
        } else if (diffDay == 1) {
            returnDate = " " + "today";
        } else if (diffDay == 2) {
            returnDate = " " + "yesterday";
        } else if (diffDay > 2) {
            returnDate = " in" + " " + diffDay + " days";

        }

	/*
     * if(!isGraterThanOne){ if(diffDay>0){ if(diffDay>1){ returnDate =
	 * returnDate + " "+diffDay + " Days "; }else{ returnDate = returnDate +
	 * " "+diffDay + " Day "; } }else if(diffDay<0){ if(diffDay<-1){
	 * returnDate = returnDate + " "+diffDay + " Days "; }else{ returnDate =
	 * returnDate + " "+diffDay + " Day "; } }
	 */
        if (returnDate.equalsIgnoreCase("")) {
            if (hours > 0) {
                if (hours > 1) {
                    returnDate = "Today";
                } else {
                    returnDate = "Today ";
                }
            } else if (hours < 0) {
                if (hours < -1) {
                    returnDate = "Today ";
                } else {
                    returnDate = "Today";
                }
            }

            if (returnDate.equalsIgnoreCase("")) {
                if (minutes > 0) {
                    if (minutes > 1) {
                        returnDate = "Today";
                    } else {
                        returnDate = "Today";
                    }
                } else if (minutes < 0) {
                    if (minutes < -1) {
                        returnDate = "Today";
                    } else {
                        returnDate = "Today";
                    }
                } else {
                    returnDate = "Today";
                }
            }
        }

        return returnDate;
    }

    private void showDialog(String title,String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);

        String positiveText = "Close";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }
    private boolean isSchemaPresent(String text) {
        if(text.startsWith("@")){
            return true;
        }
        return false;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_WITH_LIST_VACCINATION;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }





}