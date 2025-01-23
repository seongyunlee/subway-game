#!/bin/bash

# Set the AWS region (you can customize this or retrieve it dynamically)
AWS_REGION="ap-northeast-2a"

# Step 1: Get the list of all RDS instances
echo "Retrieving all RDS instances..."
RDS_INSTANCES=$(aws rds describe-db-instances --region "$AWS_REGION" | jq -r '.DBInstances[].DBInstanceIdentifier')

echo "Found RDS instances: $RDS_INSTANCES"

# Step 2: Iterate over each RDS instance and check public accessibility
for RDS_INSTANCE_ID in $RDS_INSTANCES; do
    echo "Checking RDS instance: $RDS_INSTANCE_ID"

    # Get details of the current RDS instance
    RDS_DETAILS=$(aws rds describe-db-instances --db-instance-identifier "$RDS_INSTANCE_ID" --region "$AWS_REGION")

    # Extract public accessibility and endpoint information
    IS_PUBLICLY_ACCESSIBLE=$(echo $RDS_DETAILS | jq -r '.DBInstances[0].PubliclyAccessible')
    DB_ENDPOINT=$(echo $RDS_DETAILS | jq -r '.DBInstances[0].Endpoint.Address')
    DB_MASTER_USERNAME=$(echo $RDS_DETAILS | jq -r '.DBInstances[0].MasterUsername')

    echo "RDS Instance Identifier: $RDS_INSTANCE_ID"
    echo "Endpoint: $DB_ENDPOINT"

    if [ "$IS_PUBLICLY_ACCESSIBLE" == "true" ]; then
        echo "The RDS instance is publicly accessible."
    else
        echo "The RDS instance is not publicly accessible. Modifying settings..."

        # Modify the RDS instance to be publicly accessible
        aws rds modify-db-instance --db-instance-identifier "$RDS_INSTANCE_ID" --publicly-accessible --apply-immediately --region "$AWS_REGION"

        echo "The RDS instance is now set to be publicly accessible."
    fi

    # Print the DB master username (you can't retrieve the password via CLI)
    echo "DB Master Username: $DB_MASTER_USERNAME"
    echo "Note: Password retrieval is not supported via AWS CLI. Please check your secrets manager or secure storage."

    echo "-------------------------------------------------"
done
