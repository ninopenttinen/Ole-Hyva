package com.example.ole;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ole.components.FavoriteRecipe;
import com.example.ole.model.Recipe;
import com.example.ole.viewmodel.FavoritesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoritesView extends AppCompatActivity {

  private final List<FavoriteRecipe> favItemArrayList = new ArrayList<FavoriteRecipe>();
  private final List<HashMap<String, String>> favItemHashMap = new ArrayList<>();
  FavoritesViewModel favoritesViewModel;
  SimpleAdapter simpleAdapter;
  ListView favListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);

    favoritesViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication()))
        .get(FavoritesViewModel.class);

    favoritesViewModel.getRecipes().observe(this, this::updateFavorites);
  }

  private void updateFavorites(List<Recipe> favRecipes) {
    for (int i = 0; i < favRecipes.size(); i++) {
      favItemArrayList.add(new FavoriteRecipe(
          favRecipes.get(i).getLabel(),
          favRecipes.get(i).getUrl() // TODO: getUrl IS ONLY A PLACEHOLDER, replace with image bitmap
      ));
    }

    for (FavoriteRecipe i : favItemArrayList) {
      HashMap<String, String> favoriteItemHash = new HashMap<>();
      favoriteItemHash.put("favName", i.mFavoriteRecipe);
      favoriteItemHash.put("favImg", i.mFavoriteImage);
      favItemHashMap.add(favoriteItemHash);
    }

    simpleAdapter = new SimpleAdapter(this, favItemHashMap, R.layout.favorites_list_view,
        new String[]{"favImg", "favName"},
        new int[]{R.id.fav_list_img_view, R.id.fav_label_view}
    );

    favListView = findViewById(R.id.favorites_listView);
    favListView.setAdapter(simpleAdapter);


    favListView.setOnItemClickListener((parent, view, position, id) -> {
      Intent intent = new Intent(this, RecipeView.class);
      intent.putExtra("recipe", Parcels.wrap(favRecipes.get(position)));
      startActivity(intent);
    });

    favListView.setOnItemLongClickListener((parent, view, position, id) -> {

      Recipe recipeToBeRemoved = new Recipe(
          null,
          null,
          favRecipes.get(position).getUrl(),
          null,
          null,
          null);

      BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FavoritesView.this, R.style.BottomSheetDialogTheme);
      View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_popup_layout,
          findViewById(R.id.bottomSheetContainer));

      bottomSheetDialog.setContentView(bottomSheetView);
      bottomSheetDialog.show();

      bottomSheetView.findViewById(R.id.cancel_button).setOnClickListener(v1 -> {
        Toast.makeText(FavoritesView.this, "Canceled", Toast.LENGTH_LONG).show();
        bottomSheetDialog.dismiss();
      });

      bottomSheetView.findViewById(R.id.confirm_button).setOnClickListener(v1 -> {
        removeFromFav(recipeToBeRemoved);
        favItemHashMap.remove(position);
        simpleAdapter.notifyDataSetChanged();
        Toast.makeText(FavoritesView.this, "Removed from favorites", Toast.LENGTH_LONG).show();
        bottomSheetDialog.dismiss();
      });
      return true;
    });
  }

  public void removeFromFav(Recipe rec) {
    favoritesViewModel.removeRecipeFromFavourites(rec);
  }
}