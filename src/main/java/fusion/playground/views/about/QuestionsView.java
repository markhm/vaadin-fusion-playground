package fusion.playground.views.about;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fusion.playground.data.service.QuestionService;

// @Route(value = "questions")
// @PageTitle("Questions")
// @CssImport("./views/about/about-view.css")
public class QuestionsView extends VerticalLayout
{
    public QuestionsView(QuestionService service)
    {
        setId("questions-view");

//        Grid<Question> grid = new Grid<>(Question.class);
//        grid.setItems(service.findAll());
//        add(grid);
//        setSizeFull();

    }

}
