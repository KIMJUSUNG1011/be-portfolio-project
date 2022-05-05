## 수정사항을 깃허브에 올리는 경우
- 기본적으로 로컬의 브랜치는 항상 __dev_back__ 으로 유지되어 있어야함.<br/><br/>
_새로운 브랜치를 생성할 때 dev_back 브랜치에서 생성해야 새로운 브랜치가 dev_back 에서 파생된 브랜치가 되고, 그래야 새로운 브랜치를 깃허브에 올렸을 때 pull request(새로운 브랜치를 dev_back 으로 merge) 가 가능함_<br/><br/>

- 코드를 수정하고 이를 깃허브에 반영해야하는 경우, 다음과 같은 과정을 진행한다.
~~~
0. git checkout dev_back		// 이미 dev_back 브랜치라면 진행하지 않아도 됨
1. git checkout -b 'new_branch'		// 새로운 브랜치 생성 후 이동
2. 코드 수정 및 작업 진행                    // 즉, 코드 수정 작업은 항상 새로운 브랜치에서 진행되어야함 
3. git status				// 수정사항 확인
4. git add .
5. git commit -m "commit messages"
6. git push origin 'new_branch'

/*
   여기까지 진행하면 깃 허브 사이트에서 pull request 요청이 뜰 것이다.
   클릭 후 pull reqeust 를 요청하면 화면이 전환된다.(dev_back 으로 merge 되는 것인지 확인)
   여기서 다른 팀원이(혹은 자신이) merge pull request 를 클릭하면 정상적으로 새로운 
   수정사항(new_branch)이 dev_back 에 merge 된다.(깃 허브 상에서)
   이제 다시 로컬로 돌아간다.
*/

7. git checkout dev_back
8. git pull origin dev_back		// 깃 허브에서 merge 된 사항을 로컬로 내려받음
9. git branch -d 'new_branch"		// 새로운 브랜치 삭제
~~~
<br/>

## 깃허브에서 내려받는 경우
- 현재 로컬에서 새로운 브랜치(new_branch)를 따서 작업을 진행중인 경우(git status 로 확인)
~~~
0. git stash			// 작업 중인 내역을 스택에 임시 저장해둠
1. git pull origin dev_back	// 깃 허브의 dev_back 최신버전 내려받음
2. git merge dev_back		// 충돌 발생 시 직접 수정
3. git stash pop		// 작업 중이던 내역을 스택에서 꺼냄
4. 다시 작업 진행
~~~

- 작업을 진행하는 중이 아니라면 위 과정의 1 번만 진행하면 됨
<br/><br/>

## 자주 하는 실수
1. 새로운 코드 수정 작업을 할 때는 반드시 새로운 브랜치를 따서 진행해야함
2. 새로운 브랜치는 반드시 __dev_back__ 으로 이동한 뒤 만들어야함
3. 수정사항을 깃허브로 올린 뒤 반드시 __dev_back__ 을 다시 로컬로 내려받아서 로컬에도 반영시켜야함
<br/>

## 참고 사이트
https://wayhome25.github.io/git/2017/07/08/git-first-pull-request-story/<br/>
https://chanhuiseok.github.io/posts/git-3/
