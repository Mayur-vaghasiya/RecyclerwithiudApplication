package peacocktech.in.recyclerwithiudapplication.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import peacocktech.in.recyclerwithiudapplication.R;
import peacocktech.in.recyclerwithiudapplication.recyclerview.Datahelper.DataBaseHepler;

/**
 * Created by peacock on 5/13/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> /*implements Filterable*/ {


    private final static int FADE_DURATION = 1100;// in milliseconds
    private Context context;
    private ArrayList<Friend> friends;
    private ArrayList<Friend> listfriOrigin;
    private Filter planetFilter;
    private AlertDialog.Builder build, build1, build2;
    private DataBaseHepler dhelper;
    private int gender;
    private int id;
    private LayoutInflater mInflater;

    public RecyclerAdapter(Context context, ArrayList<Friend> friends) {
        this.friends = friends;
        this.context = context;
        this.listfriOrigin = new ArrayList<Friend>();
        this.listfriOrigin.addAll(friends);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder

        View view = mInflater.inflate(R.layout.item_recycler, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder viewHolder, final int position) {
        Friend f = friends.get(position);
        //setting data to view holder elements
        viewHolder.name.setText(friends.get(position).getName());
        viewHolder.job.setText(friends.get(position).getJob());

        if (friends.get(position).getGender() == 0) {
            viewHolder.imageView.setImageResource(R.mipmap.male);
        } else {
            viewHolder.imageView.setImageResource(R.mipmap.female);
        }
        //set on click listener for each element
        viewHolder.container.setOnClickListener(onClickListener(position));
        final CharSequence[] items = {"Remove Data", "Update Record "};

        viewHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                dhelper = new DataBaseHepler(v.getContext());
                build = new AlertDialog.Builder(v.getContext());

                build.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            build1 = new AlertDialog.Builder(v.getContext());
                            build1.setTitle("Remove Data");
                            build1.setIcon(R.drawable.garbage);
                            build1.setMessage("Are you sure to Remove Record");
                            build1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // int pos = viewHolder.getAdapterPosition();
                                    Friend f = friends.get(position);
                                    int id = friends.get(position).getId();
                                    String name = friends.get(position).getName().toString();
                                    String job = friends.get(position).getJob().toString();
                                    int gender = friends.get(position).getGender();
                                    String gendr;
                                    if (gender == 0) {
                                        gendr = "male";
                                    } else {
                                        gendr = "female";
                                    }
                                    dhelper.deleteitem(id);
                                    friends.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();

                                    Toast.makeText(viewHolder.itemView.getContext(), "  Delete " + position + " " + id + " " + name + " " + job + " " + gendr, Toast.LENGTH_SHORT).show();
                                }
                            });
                            build1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            build1.create().show();
                        } else {

                            build2 = new AlertDialog.Builder(v.getContext());
                            build2.setTitle("Update Record");
                            build2.setIcon(R.drawable.update);
                            build2.setMessage("Are you sure to Update Record");
                            build2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // int pos = viewHolder.getAdapterPosition();
                                    final Friend f = friends.get(position);
                                    id = f.getId();

                                    final Dialog dialog1 = new Dialog(viewHolder.container.getContext());
                                    dialog1.setContentView(R.layout.dialog_add); //layout for dialog
                                    dialog1.setCancelable(false); //none-dismiss when touching outside Dialog

                                    // set the custom dialog components - texts and image
                                    final EditText name = (EditText) dialog1.findViewById(R.id.name);
                                    final EditText job = (EditText) dialog1.findViewById(R.id.job);
                                    Spinner spnGender = (Spinner) dialog1.findViewById(R.id.gender);
                                    View btnAdd = dialog1.findViewById(R.id.btn_ok);
                                    View btnCancel = dialog1.findViewById(R.id.btn_cancel);

                                    //set spinner adapter

                                    final ArrayList<String> gendersList = new ArrayList<>();
                                    gendersList.add("Male");
                                    gendersList.add("Female");
                                    ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(viewHolder.container.getContext(),
                                            android.R.layout.simple_dropdown_item_1line, gendersList);
                                    spnGender.setAdapter(spnAdapter);

                                    //set handling event for 2 buttons and spinner
                                    spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position == 0) {
                                                gender = 0;
                                            } else {
                                                gender = 1;
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });


                                    name.setText(f.getName().toString().trim());
                                    job.setText(f.getJob().toString().trim());

                                    if (f.getGender() == 0) {
                                        spnGender.setSelection(0);
                                    } else {
                                        spnGender.setSelection(1);
                                    }


                                    btnAdd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Friend f = new Friend(id, name.getText().toString().trim(), gender, job.getText().toString().trim());

                                            //Update  object in arraylist
                                            dhelper = new DataBaseHepler(v.getContext());
                                            dhelper.updatedata(f);

                                            if (f != null) {
                                                Toast.makeText(v.getContext(), "Update Record", Toast.LENGTH_SHORT).show();
                                            }
                                            //notify data set changed in RecyclerView adapter
                                            friends.get(position).setName(name.getText().toString().trim());
                                            friends.get(position).setJob(job.getText().toString().trim());
                                            friends.get(position).setGende(gender);

                                            notifyDataSetChanged();
                                            //close dialog after all
                                            dialog1.dismiss();

                                        }
                                    });

                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog1.dismiss();
                                        }
                                    });
                                    dialog1.show();

                                }
                            });
                            build2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            build2.create().show();
                        }
                    }
                });
                build.create().show();
                return true;
            }
        });
        //  setFadeAnimation(viewHolder.itemView);
        setScaleAnimation(viewHolder.itemView);

        // viewHolder.itemView.setBackgroundColor(COLORS[(int) (f.getId() % COLORS.length)]);
        if (position % 2 == 0) {
            viewHolder.itemView.setBackgroundColor(Color.argb(255, 200, 213, 205));
            viewHolder.imageView.setBackgroundColor(Color.argb(255, 200, 213, 225));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.argb(255, 200, 213, 225));
            viewHolder.imageView.setBackgroundColor(Color.argb(255, 200, 213, 205));
        }

    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setDataToView(TextView name, TextView job, ImageView genderIcon, int position) {
        name.setText(friends.get(position).getName());
        job.setText(friends.get(position).getJob());
        if (friends.get(position).getGender() == 0) {
            genderIcon.setImageResource(R.mipmap.male);
        } else {
            genderIcon.setImageResource(R.mipmap.female);
        }
    }

    @Override
    public int getItemCount() {
        return (null != friends ? friends.size() : 0);
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_recycler);
                dialog.setTitle("Position " + position);
                dialog.setCancelable(true); // dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                TextView name = (TextView) dialog.findViewById(R.id.name);
                TextView job = (TextView) dialog.findViewById(R.id.job);
                ImageView icon = (ImageView) dialog.findViewById(R.id.image);
                setDataToView(name, job, icon, position);
                dialog.show();

            }
        };
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        friends.clear();

        if (charText.length() < 0) {
            friends.addAll(listfriOrigin);
        } else {
            for (Friend pic : listfriOrigin) {
                if ((pic.getName().toUpperCase().startsWith(charText.toString().toUpperCase())) || (pic.getJob().toUpperCase().startsWith(charText.toUpperCase()))) {
                    friends.add(pic);
                }
            }
        }
        notifyDataSetChanged();
    }


    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView job;
        private View container;

        public ViewHolder(final View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            job = (TextView) view.findViewById(R.id.job);
            container = view.findViewById(R.id.card_view);


        }
    }
}