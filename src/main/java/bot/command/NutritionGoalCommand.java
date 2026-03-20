package bot.command;

import domain.NutritionGoals;
import lombok.RequiredArgsConstructor;
import service.NutritionGoalsService;
import service.UserService;

@RequiredArgsConstructor
public class NutritionGoalCommand implements Command {
    private final NutritionGoalsService nutritionGoalsService;
    private final UserService userService;

    @Override

    public String execute(Long id, String... args) {
        try {
            Double calories = Double.parseDouble(args[0]);
            var user = userService.findByTelegramId(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            if (user.getNutritionGoals() == null) {
                NutritionGoals nutritionGoals = NutritionGoals.builder()
                        .user(user)
                        .calories(calories)
                        .build();

                nutritionGoalsService.save(nutritionGoals);

                user.setNutritionGoals(nutritionGoals);
                return "Цель установлена: " + calories + " ккал";
            } else {
                return "У вас уже установлена цель: " +
                        user.getNutritionGoals().getCalories() + " ккал";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Во времея установки цели произошла ошибка" + e.getMessage();
        }
    }
}
