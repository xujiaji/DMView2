package com.xujiaji.dmlib2;
/*
 * Copyright 2018 xujiaji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.view.View;
import com.xujiaji.dmlib2.entity.BaseDmEntity;
import java.util.PriorityQueue;

/**
 * 弹幕接口
 */
public interface DM
{

    /**
     * 添加一个弹幕
     * @param templateView 模板View
     */
    void add(View templateView);

    void add(BaseDmEntity entity);

}
