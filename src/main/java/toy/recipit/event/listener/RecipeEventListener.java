package toy.recipit.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.event.RecipeViewEvent;
import toy.recipit.mapper.RecipeMapper;

@Component
@RequiredArgsConstructor
public class RecipeEventListener {
    private final RecipeMapper recipeMapper;

    @Async
    @Transactional
    @EventListener
    public void handleRecipeViewEvent(RecipeViewEvent event) {
        recipeMapper.upsertRecentRecipe(event.getUserNo(), event.getRecipeNo());
    }
}