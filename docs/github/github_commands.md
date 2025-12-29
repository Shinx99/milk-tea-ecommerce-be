


#            🚀 GitHub Commands Cheat Sheet 🚀                #


---


# 🌟 --- Setup & Config --- 🌟
```bash
git config --global user.name "Your Name"           # Set your username
git config --global user.email "you@example.com"    # Set your email
git config --list                                  # List all configs
```
# 🌱 --- Starting a Repo --- 🌱
```bash
git init                                           # Start new local repo
git clone <url>                                    # Clone repo from GitHub
```
# 🛠️ --- Basic Workflow --- 🛠️
```bash
git status                                         # Check status
git add <file>                                     # Stage a file
git add .                                          # Stage all changes
git commit -m "message"                            # Commit staged changes
git log                                            # Show commit history
```
# 🌳 --- Branching --- 🌳
```bash
git branch                                         # List branches
git branch <name>                                  # Create new branch
git checkout <name>                                # Switch to branch
git checkout -b <name>                             # Create + switch branch
git merge <branch>                                 # Merge branch into current
git branch -d <name>                               # Delete branch
```
# 🌏 --- Remote (GitHub) --- 🌏
```bash
git remote -v                                      # Show remotes
git remote add origin <url>                        # Add remote origin
git push -u origin main                            # Push first time
git push                                           # Push changes
git pull                                           # Pull latest changes
git fetch                                          # Fetch branches/tags
```
# 🌀 --- Undo & Fix --- 🌀
```bash
git checkout -- <file>                             # Discard changes (before staging)
git reset HEAD <file>                              # Unstage a file
git reset --hard HEAD                              # Reset to last commit (a loses changes)
git revert <commit>                                # New commit that undoes a commit
```
# 💾 --- Stash --- 💾
```bash
git stash                                          # Save uncommitted changes
git stash pop                                      # Reapply last stash
git stash list                                     # Show stashes
```
# 🔖 --- Tags --- 🔖
```bash
git tag v1.0                                       # Create tag
git tag                                            # List tags
git push origin v1.0                               # Push tag to GitHub
```

---
#                ✨ Happy Coding on GitHub! ✨                 #

---