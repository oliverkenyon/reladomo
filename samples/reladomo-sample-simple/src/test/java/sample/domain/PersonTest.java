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

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.finder.Operation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonTest
        extends AbstractReladomoTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonTest.class);

    @Test
    public void uniqueIndexViolation() throws Exception {

        insertRow(1, "name 1");
        insertRow(2, "name 2");

        MithraManager.getInstance().executeTransactionalCommand(tx -> {
            updateFirstName("new name 1", 1);
            insertRow(3, "name 1");
            updateFirstName("new name 2",2);

            return null;
        });
    }

    @Test
    public void uniqueIndexWithUpdatesAboveInsert() throws Exception {

        insertRow(1, "name 1");
        insertRow(2, "name 2");

        MithraManager.getInstance().executeTransactionalCommand(tx -> {
            updateFirstName("new name 1", 1);
            updateFirstName("new name 2",2);
            insertRow(3, "name 1");

            return null;
        });
    }

    @Test
    public void uniqueIndexWithoutBatchedUpdate() throws Exception {

        insertRow(1, "name 1");
        insertRow(2, "name 2");

        MithraManager.getInstance().executeTransactionalCommand(tx -> {
            updateFirstName("new name 1", 1);
            insertRow(3, "name 1");

            return null;
        });
    }


    private void insertRow(int id, String name) {
        Person person = new Person();
        person.setPersonId(id);
        person.setFirstName(name);
        person.setLastName("last name");
        person.setCountry("country");
        person.insert();
    }

    private void updateFirstName(String newName, int whereId) {
        Operation operation = PersonFinder.personId().eq(whereId);
        Person existingRow = PersonFinder.findOne(operation);
        existingRow.setFirstName(newName);
    }

}
