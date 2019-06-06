package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratibha.myweathercheck.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.Forecastday;
import util.Utility;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context mCtx;
    private ArrayList<Forecastday> forecastdayArrayList;

    public WeatherAdapter(Context mCtx, ArrayList<Forecastday> forecastdayArrayList) {
        this.mCtx = mCtx;
        this.forecastdayArrayList = forecastdayArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //printing day, min temp, conditions and icon
        holder.date.setText(Utility.changeDateformat(forecastdayArrayList.get(position).getDate()));
        holder.temp.setText(Math.round(forecastdayArrayList.get(position).getDay().getMintempC()) + mCtx.getString(R.string.mydegree));
        holder.detail.setText(forecastdayArrayList.get(position).getDay().getCondition().getText());

        Picasso.with(mCtx).load("http:" + forecastdayArrayList.get(position).getDay().getCondition().getIcon())
                .error(R.drawable.ic_launcher_foreground).resize(64, 64).centerCrop()
                .into(holder.icon);


    }

    @Override
    public int getItemCount() {
        return forecastdayArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.image)
        ImageView icon;
        @BindView(R.id.detail)
        TextView detail;
        @BindView(R.id.textView)
        TextView temp;
        @BindView(R.id.dateText)
        TextView date;

        private ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}