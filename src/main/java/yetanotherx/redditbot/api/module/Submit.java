package yetanotherx.redditbot.api.module;

import yetanotherx.redditbot.RedditPlugin;
import yetanotherx.redditbot.api.APIModule;
import yetanotherx.redditbot.exception.NetworkException;
import yetanotherx.redditbot.exception.ParserException;

public class Submit extends APIModule {

    public Submit(RedditPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute() throws NetworkException, ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
