#!/bin/bash

osname=$(uname)
if [ "$osname" = "Linux" ]
then
    bin_dir=$( dirname $(readlink -f $0) )
else
    bin_dir=$( cd "$( dirname "$0" )" && pwd )
fi

clt_dir="${bin_dir}/.."

java -cp "${clt_dir}/lib/*" \
-Dconfig.file.path="${clt_dir}/config/conf.json" \
-Dlogs.dir="${clt_dir}" \
com.dbapp.Main