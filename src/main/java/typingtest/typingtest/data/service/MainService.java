package typingtest.typingtest.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import typingtest.typingtest.data.model.Text;
import typingtest.typingtest.data.model.User;
import typingtest.typingtest.data.model.UserText;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MainService {

    private UserService userService;
    private TextService textService;

    @Autowired
    public void setTextService(TextService textService) {
        this.textService = textService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Text getText(int textId) {
        return textService.getText((long) textId);
    }

    public Map<User,Double> getScoresForText(int textId) {
        Map<Long, Double> result = textService.getScoresForText((long) textId);
        Map<User, Double>  map = new TreeMap<>(Comparator.comparing(User::getId));
        if(result != null && !result.isEmpty()){
            result.forEach((aLong, aDouble) -> map.put(userService.getUser(aLong), aDouble));
        }
        return map;
    }

    public boolean addScoreForUser(String userName, int textId, Double score) {
        List<Double> tmp = textService.getBestScoresForText((long) textId);

        if(tmp.size() < 10 || score > tmp.get(tmp.size()-1)){
            User user = new User(userName);
            Text text = textService.getText((long) textId);
            UserText userText = new UserText(user, text, score);
            user.getUserTexts().add(userText);
            textService.addText(text);
            userService.addUser(user);
            return true;
        }
        return false;
    }
}
