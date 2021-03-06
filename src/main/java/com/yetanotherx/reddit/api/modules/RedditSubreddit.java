package com.yetanotherx.reddit.api.modules;

import java.util.HashMap;
import com.yetanotherx.reddit.RedditPlugin;
import com.yetanotherx.reddit.api.APIError;
import com.yetanotherx.reddit.api.APIModule;
import com.yetanotherx.reddit.api.data.SubredditData;
import com.yetanotherx.reddit.exception.APIException;
import com.yetanotherx.reddit.redditbot.http.Transport;
import com.yetanotherx.reddit.http.request.Request;
import com.yetanotherx.reddit.http.request.RequestType;
import com.yetanotherx.reddit.http.request.WebRequest;
import com.yetanotherx.reddit.http.response.JSONResult;
import com.yetanotherx.reddit.http.response.Response;
import com.yetanotherx.reddit.util.HTTPUtils;
import com.yetanotherx.reddit.util.LinkType;
import com.yetanotherx.reddit.util.MapNode;
import com.yetanotherx.reddit.util.collections.EasyHashMap;

/**
 * API module for interacting with subreddits
 * 
 * @author yetanotherx
 */
public class RedditSubreddit extends APIModule {

    protected SubredditData subreddit;
    
    protected RedditSubreddit(RedditPlugin plugin, SubredditData subreddit) {
        super(plugin);
        this.subreddit = subreddit;
    }

    /**
     * Creates a new instance for this SubredditData
     * 
     * @param plugin
     * @param sr
     * @return 
     */
    public static RedditSubreddit newFromSubreddit(RedditPlugin plugin, SubredditData sr) {
        return new RedditSubreddit(plugin, sr);
    }
    
    public static RedditSubreddit newFromID(RedditPlugin plugin, String id) {
        throw new UnsupportedOperationException("Reddit API does not support getting info about subreddits by ID right now.");
    }
    
    /**
     * Creates a new instance for this subreddit name
     * 
     * @param plugin
     * @param name
     * @return 
     */
    public static RedditSubreddit newFromName(RedditPlugin plugin, String name) {
        return new RedditSubreddit(plugin, getSubredditFromName(plugin, name));
    }
    
    protected static SubredditData getSubredditFromName(RedditPlugin plugin, String name) {
        
        Transport transport = plugin.getTransport();
        
        Request request = new WebRequest(plugin);
        request.setURL(plugin.getRedditURL() + "/r/" + HTTPUtils.urlEncode(name) + "/about.json");
        transport.setRequest(request);
        
        Response response = transport.sendURL();
        JSONResult json = response.getJSONResult();
        
        MapNode localData = json.getMapNode("data");
        if( localData != null ) {
            return SubredditData.newInstance(localData);
        }
        return null;
    }

    /**
     * Returns the SubredditData instance
     * @return 
     */
    public SubredditData getSubredditData() {
        return subreddit;
    }
    
    /**
     * Submits a new link to this subreddit
     * 
     * @param plugin
     * @param title
     * @param content
     * @param subreddit
     * @param type
     * @return 
     */
    public boolean doSubmit(String title, String content, LinkType type) {
        return RedditLink.doSubmit(plugin, title, content, content, type);
    }
    
    /**
     * Adds flair to a user. 
     * 
     * @param user
     * @param css
     * @param text
     * @return 
     */
    public boolean doAddFlair(String user, String css, String text) {
        Transport transport = plugin.getTransport();

        HashMap<String, String> map = new EasyHashMap<String, String>(
                "r", subreddit,
                "name", user,
                "text", text,
                "css_class", css,
                "uh", plugin.getModHash());

        Request request = new WebRequest(plugin, map);
        request.setURL(plugin.getRedditURL() + "/api/flair");
        request.setMethod(RequestType.POST);
        transport.setRequest(request);

        Response response = transport.sendURL();
        String error = APIModule.getError(response.getContent());

        if (error != null) {
            throw new APIException(APIError.realValueOf(error).getMessage());
        }
        
        return true;
    }
    
}
