package org.redisson;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.Test;
import org.redisson.api.RFuture;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.connection.ServiceManager;
import org.redisson.misc.CompletableFutureWrapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public class RedissonSearchCommandTest {

    @Test
    public void testListAliasesAsyncUsesAliasListCommand(@Mocked CommandAsyncExecutor commandExecutor,
                                                        @Mocked ServiceManager serviceManager) {
        List<String> aliases = Collections.singletonList("alias-list-name");
        RFuture<List<String>> future = new CompletableFutureWrapper<>(aliases);

        new Expectations() {{
            commandExecutor.getServiceManager();
            result = serviceManager;
            serviceManager.getCodec(StringCodec.INSTANCE);
            result = StringCodec.INSTANCE;
            commandExecutor.readAsync((String) null, StringCodec.INSTANCE, RedisCommands.FT_ALIASLIST);
            result = future;
        }};

        RedissonSearch search = new RedissonSearch(StringCodec.INSTANCE, commandExecutor);

        assertSame(future, search.listAliasesAsync());
    }
}
