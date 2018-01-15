/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package sample.domain;

import com.gs.fw.common.mithra.test.ConnectionManagerForTests;
import com.gs.fw.common.mithra.test.MithraTestResource;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractReladomoTest
{
    private MithraTestResource mithraTestResource;

    protected String getMithraConfigXmlFilename()
    {
        return "testconfig/TestMithraRuntimeConfig.xml";
    }

    @Before
    public void setUp() throws Exception
    {
        this.mithraTestResource = new MithraTestResource(this.getMithraConfigXmlFilename());

        final ConnectionManagerForTests connectionManager = ConnectionManagerForTests.getInstanceForDbName("testdb");
        this.mithraTestResource.createSingleDatabase(connectionManager);
        this.mithraTestResource.setUp();

        mithraTestResource.getDatabaseObjectPerConnectionManager().get(connectionManager).forEach(mithraDatabaseObject -> {
            executeSqlStatement("CREATE UNIQUE INDEX IF NOT EXISTS idx_0 ON PERSON (FIRST_NAME)", connectionManager.getConnection());
        });
    }

    private static void executeSqlStatement(String query, Connection targetConn){
        Statement stm = null;
        try {
            stm = targetConn.createStatement();
            stm.execute(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                stm.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @After
    public void tearDown() throws Exception
    {
        this.mithraTestResource.tearDown();
    }
}
