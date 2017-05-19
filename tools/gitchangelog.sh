#!/bin/bash

# Define default variables
current_branch=$(git symbolic-ref --short -q HEAD)
option_m=$(git rev-list --no-merges $current_branch)
curr_version=""
next_version=""


usage() { echo "Usage: [-h <boolean>] [-m <boolean>] [-b <string>]"; exit 1; }

help="This is the help of gitchangelog generator, you can use the next options: \n
			-h		print kthis help \n
			-m 		generate changelog with merges ( !--no-merges ) \n
			-b		from specific branch, default actual branch"

while getopts "mhb:" option; do
    case $option in
 				h)
            echo $help
						exit $E_OPTERROR;
            ;;
        m)   						         
						option_m=$(git rev-list $current_branch)
            ;;
				b)	
						if [ `git branch | grep $OPTARG` ];	then
								option_m=$(git rev-list --no-merges $OPTARG)
						else
								echo "Branch named $OPTARG does not exist!"; exit 1;
						fi						
            ;;
        *)
            usage
            ;;
    esac
done

# If CHANGELOG.md file exist remove
if [ -f CHANGELOG.md ]; then
	# Make backup of changelog
	cp CHANGELOG.md CHANGELOG_OLD.md && rm CHANGELOG.md	
	echo "Generate CHANGELOG.md..."
else 
	echo "CHANGELOG.md file is not exist! Generate new..."
fi

# Add title of file
echo "# CHANGELOG.md PSG-16 Front End \n " > CHANGELOG.md;

# Add the date
echo "## Generate in: $(date) \n " >> CHANGELOG.md;

# Generate CHANGELOG.md, init loop for commit of current branch length
for commit in $option_m; do

	# Get current commit version
	curr_version=$(git --no-pager grep -h "\"version\"" $commit -- package.json)
	
	# If current version is equal of next version
	if [ "$curr_version" = "$next_version" ]; then
		# Add only commit comment
		echo $( git log -n 1 --pretty=format:"- %s" $commit ) >> CHANGELOG.md;
	else
		# Else add number of version and commit comment
		echo "\n" >> CHANGELOG.md;
		echo "### Version" $( git --no-pager grep -h "\"version\"" $commit -- package.json | cut -d'"' -f 4) >> CHANGELOG.md;
		echo $( git log -n 1 --pretty=format:"- %s" $commit ) >> CHANGELOG.md;
	fi
	
	# Get next version number
	next_version=$(git --no-pager grep -h "\"version\"" $commit -- package.json)
	
done

# If has been successfully generated remove old changelog
if [ -f CHANGELOG.md ];	then 
	echo "The CHANGELOG.md has been successfully generated" 
	if [ -f CHANGELOG_OLD.md ]; then
		rm CHANGELOG_OLD.md		
	fi
else
	# Else backup old changelog
	mv CHANGELOG_OLD.md CHANGELOG.md && echo "Error..."	
fi; 