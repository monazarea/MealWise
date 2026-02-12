package com.example.mealwise.presentation.planner.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.di.Injection;
import com.example.mealwise.presentation.planner.presenter.PlannerPresenter;
import com.example.mealwise.presentation.planner.presenter.PlannerPresenterImpl;
import com.example.mealwise.utils.AuthDialog;
import com.example.mealwise.utils.DeleteDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlannerFragment extends Fragment implements PlannerView {

    private PlannerPresenter presenter;
    private RecyclerView rvPlanner;
    private PlannerAdapter adapter;
    private CalendarView calendarView;
    private ConstraintLayout emptyStateLayout;
    private ImageView btnBack;
    private TextView tvEmpty;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        setupRecyclerView();

        presenter = new PlannerPresenterImpl(
                this,
                Injection.provideMealsRepository(requireContext()),
                Injection.provideAuthRepository(requireContext())
        );

        presenter.getPlannedByDay(getCurrentDayName());

        calendarView.setOnDateChangeListener((cv, year, month, dayOfMonth) -> {
            String selectedDay = getFormattedDate(year, month, dayOfMonth);
            presenter.getPlannedByDay(selectedDay);
        });

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    private void initViews(View view) {
        rvPlanner = view.findViewById(R.id.rvPlanner);
        calendarView = view.findViewById(R.id.calendarView);
        emptyStateLayout = view.findViewById(R.id.plannerEmptyState);
        btnBack = view.findViewById(R.id.btnBack);
        tvEmpty = view.findViewById(R.id.tvEmpty);

    }

    private void setupRecyclerView() {
        adapter = new PlannerAdapter(new PlannerAdapter.OnPlanClickListener() {
            @Override
            public void onRemoveClick(Meal meal) {
                DeleteDialogUtil.showDeleteConfirmation(requireContext(), meal.getName(), () -> {
                    presenter.removeFromPlanned(meal);
                });            }
            @Override
            public void onMealClick(Meal meal) {
                PlannerFragmentDirections.ActionPlannerFragment2ToDetailsFragment action =
                        PlannerFragmentDirections.actionPlannerFragment2ToDetailsFragment(meal);

                Navigation.findNavController(requireView()).navigate(action);
            }
        });

        rvPlanner.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPlanner.setAdapter(adapter);
    }



    private String getDayNameFromDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        String dayName = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
        Log.d("asd", "Converted " + dayOfMonth + "/" + (month+1) + " to: " + dayName);
        return dayName;
    }

    private String getCurrentDayName() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());    }

    @Override
    public void showPlanMeals(List<Meal> meals) {

        rvPlanner.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        adapter.setList(meals);
    }

    @Override
    public void showEmptyState() {
        rvPlanner.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRemoveSuccess(Meal meal) {
        Toast.makeText(requireContext(), "Removed: " + meal.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGuestWarning() {
        AuthDialog.showGuestFavoriteDialog(requireContext(), Navigation.findNavController(requireView()));
        tvEmpty.setText("Sign in to see and save your favorite meals!");    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        Log.d("asd",message);

    }
    private String getFormattedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}