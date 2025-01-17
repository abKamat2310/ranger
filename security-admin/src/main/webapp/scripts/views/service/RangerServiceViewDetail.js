/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


define(function(require) {
        'use strict';

        var Backbone = require('backbone');
        var XAEnums = require('utils/XAEnums');
        var XAGlobals = require('utils/XAGlobals');
        var XAUtils = require('utils/XAUtils');
        var localization = require('utils/XALangSupport');

        var RangerServiceViewDetailTmpl = require('hbs!tmpl/service/RangerServiceViewDetail_tmpl');
        var RangerService = require('models/RangerService');

        var RangerServiceView = Backbone.Marionette.Layout.extend({
                _viewName: 'RangerServiceView',

                template: RangerServiceViewDetailTmpl,
                templateHelpers: function() {
                    var that = this;

                    return {
               configsList : this.conf,
               customConfigs : this.customConfigs,
               serviceName : this.options.rangerService.get('name'),
               description : this.options.rangerService.get('description'),
               isEnabled   : this.options.rangerService.get('isEnabled'),
               tagService  : (this.options.rangerService.get('tagService')) ? this.options.rangerService.get('tagService') : false,
               displayName : this.options.rangerService.get('displayName'),
           }
                },

                /**
                 * intialize a new RangerServiceDiffDetaile Layout
                 * @constructs
                 */
                initialize: function(options) {
                    console.log("initialized a Ranger Service View Diff");
                    var that = this;
                    that.getTemplateForservice(this.options);
                },
                getTemplateForservice : function(options){
                    var configList = options.serviceDef.get('configs');
                    var serviceConfigs = options.rangerService.get('configs');
                    var configs = {} , customConfigs = serviceConfigs;
                    _.each(configList , function(m){
                        if(m.label){
                            configs[m.label] = serviceConfigs[m.name]
                        }else{
                            configs[m.name] = serviceConfigs[m.name]
                        }
                        customConfigs = _.omit(customConfigs , m.name);
                    })
                    this.conf = configs;
                    if(_.isEmpty(customConfigs)){
                        this.customConfigs = false
                    }else{
                        this.customConfigs = customConfigs;
                    }
                },
                /** on close */
                onClose: function() {}
        });

        return RangerServiceView;
});
