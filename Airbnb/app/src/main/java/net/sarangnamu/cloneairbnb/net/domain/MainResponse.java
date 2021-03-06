/*
 * Copyright 2016. Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sarangnamu.cloneairbnb.net.domain;

import net.sarangnamu.cloneairbnb.net.resource.Famous;
import net.sarangnamu.cloneairbnb.net.resource.Recommandation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2016. 4. 28.. <p/>
 */
public class MainResponse implements Serializable {
    private static final long serialVersionUID = 3586105721745211832L;

    private List<Recommandation> recommandationList;
    private List<Famous> famousList;

    public List<Recommandation> getRecommandationList() {
        return recommandationList;
    }

    public List<Famous> getFamousList() {
        return famousList;
    }
}
