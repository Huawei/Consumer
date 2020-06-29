/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

#import "ViewController.h"
#import "SettingsViewController.h"
#import "HiAnalytics/HiAnalytics.h"

@interface ViewController ()
@property (weak, nonatomic) IBOutlet UILabel *question;

@property (weak, nonatomic) IBOutlet UIButton *settings;
@property (weak, nonatomic) IBOutlet UIButton *answerTrue;
@property (weak, nonatomic) IBOutlet UIButton *answerFalse;

@property(nonatomic, weak) IBOutlet UIView *uiView;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [_settings addTarget:self action:@selector(clickMe:) forControlEvents:UIControlEventTouchUpInside];
    [_answerTrue addTarget:self action:@selector(reportAnswer:) forControlEvents:UIControlEventTouchUpInside];
    [_answerFalse addTarget:self action:@selector(reportAnswer:) forControlEvents:UIControlEventTouchUpInside];
    
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

-(void)clickMe:(id)sender{
 
  [self presentViewController:[[SettingsViewController alloc] init] animated:true completion:nil];
}

-(void) reportAnswer:(id)sender{
    //set user name
       [HiAnalytics setUserId:@"Li Lei"];
    UIButton *button = (UIButton *)sender;
    NSString *mesage = [button currentTitle];
    NSDateFormatter *nsdf=[[NSDateFormatter alloc] init];
    [nsdf setDateStyle:NSDateFormatterShortStyle];
    [nsdf setDateFormat:@"YYYY/MM/dd HH:mm:ss"];
    NSString *time=[nsdf stringFromDate:[NSDate date]];
    
    //report event
   [HiAnalytics onEvent:@"QuestionAnsweringEvent" setParams:@{@"Answer":mesage,@"Timestamp":time}];
}

@end
